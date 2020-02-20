package dev.polek.episodetracker.common.repositories

import dev.polek.episodetracker.common.datasource.themoviedb.TmdbService
import dev.polek.episodetracker.common.datasource.themoviedb.TmdbService.Companion.backdropImageUrl
import dev.polek.episodetracker.common.datasource.themoviedb.TmdbService.Companion.networkImageUrl
import dev.polek.episodetracker.common.datasource.themoviedb.TmdbService.Companion.stillImageUrl
import dev.polek.episodetracker.common.datasource.themoviedb.entities.EpisodeEntity
import dev.polek.episodetracker.common.datasource.themoviedb.entities.GenreEntity
import dev.polek.episodetracker.common.logging.log
import dev.polek.episodetracker.common.presentation.myshows.model.MyShowsListItem
import dev.polek.episodetracker.common.utils.formatEpisodeNumber
import dev.polek.episodetracker.common.utils.formatTimeBetween
import dev.polek.episodetracker.db.Database
import dev.polek.episodetracker.db.ShowDetails
import io.ktor.util.date.GMTDate

class MyShowsRepository(
    private val db: Database,
    private val tmdbService: TmdbService)
{
    suspend fun addShow(tmdbId: Int) {
        if (isInMyShows(tmdbId)) return
        val show = tmdbService.show(tmdbId)
        check(show.isValid) { throw RuntimeException("Trying to add invalid show: $show") }

        log("Adding show: $show")

        val seasons = (1..show.numberOfSeasons).map { seasonNumber ->
            tmdbService.season(tmdbId = tmdbId, number = seasonNumber)
        }

        db.transaction {
            seasons.flatMap { it.episodes.orEmpty() }
                .filter(EpisodeEntity::isValid)
                .forEach { episode ->
                    db.episodeQueries.insert(
                        showTmdbId = tmdbId,
                        name = episode.name.orEmpty(),
                        episodeNumber = requireNotNull(episode.episodeNumber),
                        seasonNumber = requireNotNull(episode.seasonNumber),
                        airDateMillis = episode.airDateMillis,
                        imageUrl = episode.stillPath?.let(::stillImageUrl))
                }

            show.network?.let { network ->
                db.networkQueries.insertOrReplace(
                    tmdbId = network.tmdbId ?: 0,
                    name = network.name.orEmpty(),
                    imageUrl = network.logoPath?.let(::networkImageUrl)
                )
            }

            db.myShowQueries.insert(
                tmdbId = tmdbId,
                imdbId = show.externalIds?.imdbId,
                tvdbId = show.externalIds?.tvdbId,
                facebookId = show.externalIds?.facebookId,
                instagramId = show.externalIds?.instagramId,
                twitterId = show.externalIds?.twitterId,
                name = show.name.orEmpty(),
                overview = show.overview.orEmpty(),
                year = show.year,
                lastYear = show.lastYear,
                imageUrl = show.backdropPath?.let(::backdropImageUrl),
                homePageUrl = show.homepage,
                genres = show.genres?.map(GenreEntity::name).orEmpty(),
                networkTmdbId = show.network?.tmdbId,
                contentRating = show.contentRating,
                isEnded = show.isEnded,
                nextEpisodeSeason = show.nextEpisodeToAir?.seasonNumber,
                nextEpisodeNumber = show.nextEpisodeToAir?.episodeNumber)
        }
    }

    fun removeShow(tmdbId: Int) {
        db.transaction {
            db.episodeQueries.deleteByTmdbId(tmdbId)
            db.myShowQueries.deleteByTmdbId(tmdbId)
        }
    }

    fun isInMyShows(tmdbId: Int): Boolean {
        return db.myShowQueries.isInMyShows(tmdbId).executeAsOne()
    }

    fun upcomingShows(): List<MyShowsListItem.UpcomingShowViewModel> {
        val now = GMTDate()
        return db.myShowQueries.upcomingShows { tmdbId, name, episodeName, episodeNumber, seasonNumber, airDateMillis, imageUrl ->
            val daysLeft: String = if (airDateMillis != null) {
                formatTimeBetween(now, GMTDate(airDateMillis))
            } else {
                "N/A"
            }

            val show = MyShowsListItem.UpcomingShowViewModel(
                id = tmdbId,
                name = name,
                backdropUrl = imageUrl,
                episodeName = episodeName,
                episodeNumber = formatEpisodeNumber(season = seasonNumber, episode = episodeNumber),
                timeLeft = daysLeft)
            show
        }.executeAsList()
    }

    fun toBeAnnouncedShows(): List<MyShowsListItem.ShowViewModel> {
        return db.myShowQueries.toBeAnnouncedShows { tmdbId, name, imageUrl ->
            val show = MyShowsListItem.ShowViewModel(
                id = tmdbId,
                name = name,
                backdropUrl = imageUrl)
            show
        }.executeAsList()
    }

    fun endedShows(): List<MyShowsListItem.ShowViewModel> {
        return db.myShowQueries.endedShows { tmdbId, name, imageUrl ->
            val show = MyShowsListItem.ShowViewModel(
                id = tmdbId,
                name = name,
                backdropUrl = imageUrl)
            show
        }.executeAsList()
    }

    fun showDetails(tmdbId: Int): ShowDetails? {
        return db.myShowQueries.showDetails(tmdbId).executeAsOneOrNull()
    }
}
