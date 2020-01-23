package dev.polek.episodetracker.common.repositories

import dev.polek.episodetracker.common.logging.log
import dev.polek.episodetracker.common.datasource.themoviedb.TmdbService
import dev.polek.episodetracker.db.Database
import dev.polek.episodetracker.common.presentation.myshows.model.MyShowsListItem
import dev.polek.episodetracker.common.utils.millisToDate
import dev.polek.episodetracker.common.utils.millisToDays
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
            val nextEpisode = show.nextEpisodeToAir
            val nextEpisodeId = when {
                nextEpisode != null && nextEpisode.isValid -> {
                    db.nextEpisodeQueries.insert(
                        tmdbId = nextEpisode.tmdbId,
                        name = nextEpisode.name.orEmpty(),
                        episodeNumber = nextEpisode.episodeNumber ?: 0,
                        seasonNumber = nextEpisode.seasonNumber ?: 0,
                        airDateMillis = nextEpisode.airDateMillis,
                        imageUrl = if (nextEpisode.stillPath != null) TmdbService.stillImageUrl(nextEpisode.stillPath) else null)

                    db.nextEpisodeQueries.lastInsertRowId().executeAsOne()
                }
                else -> null
            }

            seasons.flatMap { it.episodes.orEmpty() }.forEachIndexed { index, episode ->
                log("Inserting $episode")
                db.episodeQueries.insert(
                    tmdbId = episode.tmdbId,
                    showTmdbId = tmdbId,
                    name = episode.name.orEmpty(),
                    episodeNumber = episode.episodeNumber ?: -1,
                    seasonNumber = episode.seasonNumber ?: -1,
                    episodeIndex = index,
                    airDateMillis = episode.airDateMillis,
                    imageUrl = if (episode.stillPath != null) TmdbService.stillImageUrl(episode.stillPath) else null)
                val id = db.episodeQueries.lastInsertRowId().executeAsOne()
                log("Inserted episode ID: $id")
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

    fun printAllShows() {
        val myShows = db.myShowQueries.selectAll { id, imdbId, tmdbId, tvdbId, facebookId, instagramId, twitterId, name, overview, year, imageUrl, isEnded, nextEpisodeId, episodeId, _, episodeName, episodeNumber, seasonNumber, airDateMillis, episodeImageUrl ->
            "$id. $name($year), Ended: $isEnded, NextEpisodeId: $nextEpisodeId, episodeId: $episodeId, poster: $imageUrl, episodeImage: $episodeImageUrl, airDate: ${airDateMillis?.millisToDate()}"
        }.executeAsList()

        log("My Shows: ${myShows.joinToString("\n")}")
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
