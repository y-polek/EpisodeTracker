package dev.polek.episodetracker.common.repositories

import dev.polek.episodetracker.common.datasource.omdb.OmdbService
import dev.polek.episodetracker.common.datasource.themoviedb.TmdbService
import dev.polek.episodetracker.common.datasource.themoviedb.TmdbService.Companion.stillImageUrl
import dev.polek.episodetracker.common.datasource.themoviedb.entities.EpisodeEntity
import dev.polek.episodetracker.common.datasource.themoviedb.entities.SeasonEntity
import dev.polek.episodetracker.common.datasource.themoviedb.entities.ShowDetailsEntity
import dev.polek.episodetracker.common.logging.log
import dev.polek.episodetracker.common.logging.logw
import dev.polek.episodetracker.common.model.Episode
import dev.polek.episodetracker.common.model.EpisodeNumber
import dev.polek.episodetracker.common.model.Season
import dev.polek.episodetracker.common.preferences.Preferences
import dev.polek.episodetracker.common.utils.now
import dev.polek.episodetracker.db.Database
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

class ShowRepository(
    private val tmdbService: TmdbService,
    private val omdbService: OmdbService,
    private val db: Database,
    private val preferences: Preferences)
{
    private var isFullRefreshInProgress = false

    suspend fun showDetails(showTmdbId: Int, noCache: Boolean = false): ShowDetailsEntity {
        return tmdbService.showDetails(showTmdbId, noCache = noCache)
    }

    suspend fun imdbRating(imdbId: String): Float? {
        return omdbService.show(imdbId).imdbRating
    }

    suspend fun season(showTmdbId: Int, seasonNumber: Int, noCache: Boolean = false): Season? {
        val seasonEntity = tmdbService.season(showTmdbId = showTmdbId, number = seasonNumber, noCache = noCache) ?: return null
        if (!seasonEntity.isValid) return null

        val season = mapSeason(seasonEntity)
        if (season.episodes.isEmpty()) return null

        return season
    }

    fun writeShowToDb(show: ShowDetailsEntity, seasons: List<Season>, markAllEpisodesWatched: Boolean = false) {
        val showTmdbId = show.tmdbId!!

        db.transaction {
            seasons.flatMap { it.episodes }
                .forEach { episode ->
                    db.episodeQueries.insert(
                        showTmdbId = showTmdbId,
                        name = episode.name,
                        episodeNumber = episode.number.episode,
                        seasonNumber = episode.number.season,
                        airDateMillis = episode.airDateMillis,
                        imageUrl = episode.imageUrl)
                }
            if (markAllEpisodesWatched) {
                db.episodeQueries.markAllWatched(showTmdbId)
            }

            db.myShowQueries.insert(
                tmdbId = showTmdbId,
                imdbId = show.externalIds?.imdbId,
                tvdbId = show.externalIds?.tvdbId,
                facebookId = show.externalIds?.facebookId,
                instagramId = show.externalIds?.instagramId,
                twitterId = show.externalIds?.twitterId,
                name = show.name.orEmpty(),
                overview = show.overview.orEmpty(),
                year = show.year,
                lastYear = show.lastYear,
                imageUrl = show.backdropPath?.let(TmdbService.Companion::backdropImageUrl),
                homePageUrl = show.homePageUrl,
                genres = show.genres,
                networks = show.networks,
                contentRating = show.contentRating,
                isEnded = show.isEnded)
        }
    }

    suspend fun refreshShow(showTmdbId: Int) {
        try {
            val show = showDetails(showTmdbId, noCache = true)
            check(show.isValid) { throw RuntimeException("Can't add invalid show: $show") }

            val seasons = show.seasonNumbers.mapNotNull { seasonNumber ->
                season(showTmdbId = showTmdbId, seasonNumber = seasonNumber, noCache = true)
            }

            updateShowInDb(show, seasons)
        } catch (e: Throwable) {
            logw { "Failed to refresh show $showTmdbId: $e" }
        }
    }

    @OptIn(ExperimentalTime::class)
    suspend fun refreshAllShows() {
        if (isFullRefreshInProgress) {
            log { "Refresh is in progress..." }
            return
        }

        isFullRefreshInProgress = true

        val shows = db.myShowQueries.allNonEndedShows().executeAsList()
        log { "Refreshing ${shows.size} shows..." }

        val time = measureTime {
            shows.forEach { showTmdbId ->
                refreshLatestSeasonAndSpecials(showTmdbId)
            }
        }
        log { "Refreshing finished in ${time.inSeconds.toInt()} sec" }
        preferences.lastRefreshTimestamp = now.timestamp
        isFullRefreshInProgress = false
    }

    private suspend fun refreshLatestSeasonAndSpecials(showTmdbId: Int) {
        val lastSeasonNumber = db.episodeQueries.lastSeason(showTmdbId).executeAsOneOrNull()?.max ?: 1

        try {
            val show = showDetails(showTmdbId, noCache = true)
            check(show.isValid) { throw RuntimeException("Can't add invalid show: $show") }

            val seasons = show.seasonNumbers
                .filter { season ->
                    season >= lastSeasonNumber || season == 0
                }
                .mapNotNull { seasonNumber ->
                    season(showTmdbId = showTmdbId, seasonNumber = seasonNumber, noCache = true)
                }

            updateShowInDb(show, seasons)
        } catch (e: Throwable) {
            logw { "Failed to refresh show $showTmdbId: $e" }
        }
    }

    private fun updateShowInDb(show: ShowDetailsEntity, seasons: List<Season>) {
        val showTmdbId = show.tmdbId!!

        db.transaction {
            seasons.flatMap { it.episodes }
                .forEach { episode ->
                    val inDb = db.episodeQueries.isEpisodeInDb(
                        showTmdbId = showTmdbId,
                        episodeNumber = episode.number.episode,
                        seasonNumber = episode.number.season).executeAsOne()
                    if (inDb) {
                        db.episodeQueries.update(
                            showTmdbId = showTmdbId,
                            name = episode.name,
                            episodeNumber = episode.number.episode,
                            seasonNumber = episode.number.season,
                            airDateMillis = episode.airDateMillis,
                            imageUrl = episode.imageUrl)
                    } else {
                        db.episodeQueries.insert(
                            showTmdbId = showTmdbId,
                            name = episode.name,
                            episodeNumber = episode.number.episode,
                            seasonNumber = episode.number.season,
                            airDateMillis = episode.airDateMillis,
                            imageUrl = episode.imageUrl)
                    }
                }

            db.myShowQueries.update(
                tmdbId = showTmdbId,
                imdbId = show.externalIds?.imdbId,
                tvdbId = show.externalIds?.tvdbId,
                facebookId = show.externalIds?.facebookId,
                instagramId = show.externalIds?.instagramId,
                twitterId = show.externalIds?.twitterId,
                name = show.name.orEmpty(),
                overview = show.overview.orEmpty(),
                year = show.year,
                lastYear = show.lastYear,
                imageUrl = show.backdropPath?.let(TmdbService.Companion::backdropImageUrl),
                homePageUrl = show.homePageUrl,
                genres = show.genres,
                networks = show.networks,
                contentRating = show.contentRating,
                isEnded = show.isEnded)
        }
    }

    companion object {

        private fun mapEpisode(episode: EpisodeEntity): Episode {
            return Episode(
                name = episode.name.orEmpty(),
                number = EpisodeNumber(
                    season = episode.seasonNumber ?: 0,
                    episode = episode.episodeNumber ?: 0),
                airDateMillis = episode.airDateMillis,
                imageUrl = episode.stillPath?.let(::stillImageUrl),
                isWatched = false
            )
        }

        private fun mapSeason(season: SeasonEntity): Season {
            return Season(
                number = season.number ?: 0,
                episodes = season.episodes?.filter(EpisodeEntity::isValid)?.map(::mapEpisode).orEmpty()
            )
        }
    }
}
