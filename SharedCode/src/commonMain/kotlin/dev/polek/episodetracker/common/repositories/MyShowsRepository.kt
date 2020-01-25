package dev.polek.episodetracker.common.repositories

import dev.polek.episodetracker.common.datasource.themoviedb.TmdbService
import dev.polek.episodetracker.common.logging.log
import dev.polek.episodetracker.common.presentation.myshows.model.MyShowsListItem
import dev.polek.episodetracker.common.utils.millisToDays
import dev.polek.episodetracker.db.Database
import io.ktor.util.date.GMTDate

class MyShowsRepository(
    private val db: Database,
    private val tmdbService: TmdbService)
{
    suspend fun addShow(tmdbId: Int) {
        if (isInMyShows(tmdbId)) return

        val show = tmdbService.showDetails(tmdbId)
        val externalIds = tmdbService.externalIds(tmdbId)
        val seasons = (1..show.numberOfSeasons).map { seasonNumber ->
            tmdbService.season(tmdbId = tmdbId, number = seasonNumber)
        }

        db.transaction {
            seasons.flatMap { it.episodes.orEmpty() }.forEach { episode ->
                log("Inserting $episode")
                db.episodeQueries.insert(
                    tmdbId = episode.tmdbId,
                    showTmdbId = tmdbId,
                    name = episode.name.orEmpty(),
                    episodeNumber = episode.episodeNumber ?: -1,
                    seasonNumber = episode.seasonNumber ?: -1,
                    airDateMillis = episode.airDateMillis,
                    imageUrl = if (episode.stillPath != null) TmdbService.stillImageUrl(episode.stillPath) else null)
                val id = db.episodeQueries.lastInsertRowId().executeAsOne()
                log("Inserted episode ID: $id")
            }

            val nextEpisodeNumber = show.nextEpisodeNumber
            val nextEpisodeId = when {
                nextEpisodeNumber != null -> {
                    db.episodeQueries.episode(
                        seasonNumber = nextEpisodeNumber.season,
                        episodeNumber = nextEpisodeNumber.episode).executeAsOneOrNull()?.id
                }
                else -> null
            }

            db.myShowQueries.insert(
                imdbId = externalIds.imdbId,
                tmdbId = tmdbId,
                tvdbId = externalIds.tvdbId,
                facebookId = externalIds.facebookId,
                instagramId = externalIds.instagramId,
                twitterId = externalIds.twitterId,
                name = show.name.orEmpty(),
                overview = show.overview.orEmpty(),
                year = show.year,
                imageUrl = if (show.posterPath != null) TmdbService.posterImageUrl(show.posterPath) else null,
                isEnded = show.isEnded,
                nextEpisodeId = nextEpisodeId)
        }
    }

    fun removeShow(tmdbId: Int) {
        db.myShowQueries.deleteByTmdbId(tmdbId)
    }

    fun isInMyShows(tmdbId: Int): Boolean {
        return db.myShowQueries.isInMyShows(tmdbId).executeAsOne()
    }

    fun upcomingShows(): List<MyShowsListItem.UpcomingShowViewModel> {
        return db.myShowQueries.upcomingShows { id, name, episodeName, episodeNumber, seasonNumber, airDateMillis, imageUrl ->
            log("$name. airDate: $airDateMillis")
            val daysLeft: String = if (airDateMillis != null) {
                val now = GMTDate().timestamp
                val millisLeft = airDateMillis - now
                log("Millis left: $millisLeft")
                val days =
                    millisToDays(millisLeft)
                "$days days"
            } else {
                "N/A"
            }

            val show = MyShowsListItem.UpcomingShowViewModel(
                name = name,
                backdropUrl = imageUrl,
                episodeName = episodeName,
                episodeNumber = "S${seasonNumber}E$episodeNumber",
                timeLeft = daysLeft)
            show
        }.executeAsList()
    }

    fun toBeAnnouncedShows(): List<MyShowsListItem.ShowViewModel> {
        return db.myShowQueries.toBeAnnouncedShows { id, name, imageUrl ->
            val show = MyShowsListItem.ShowViewModel(
                name = name,
                backdropUrl = imageUrl)
            show
        }.executeAsList()
    }

    fun endedShows(): List<MyShowsListItem.ShowViewModel> {
        return db.myShowQueries.endedShows { id, name, imageUrl ->
            val show = MyShowsListItem.ShowViewModel(
                name = name,
                backdropUrl = imageUrl)
            show
        }.executeAsList()
    }
}
