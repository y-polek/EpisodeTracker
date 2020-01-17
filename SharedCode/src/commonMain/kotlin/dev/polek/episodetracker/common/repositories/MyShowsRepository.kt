package dev.polek.episodetracker.common.repositories

import dev.polek.episodetracker.common.logging.log
import dev.polek.episodetracker.datasource.themoviedb.TmdbService
import dev.polek.episodetracker.db.Database
import dev.polek.episodetracker.myshows.model.MyShowsListItem

class MyShowsRepository(
    private val db: Database,
    private val tmdbService: TmdbService)
{
    suspend fun addShow(tmdbId: Int) {
        if (isInMyShows(tmdbId)) return

        val show = tmdbService.showDetails(tmdbId)
        val externalIds = tmdbService.externalIds(tmdbId)

        db.transaction {
            val nextEpisode = show.nextEpisodeToAir
            val nextEpisodeId = when {
                nextEpisode != null && nextEpisode.isValid -> {
                    db.nextEpisodeQueries.insert(
                        tmdbId = nextEpisode.tmdbId,
                        name = nextEpisode.name.orEmpty(),
                        episodeNumber = nextEpisode.episodeNumber ?: 0,
                        seasonNumber = nextEpisode.seasonNumber ?: 0,
                        air_date = nextEpisode.airDate.orEmpty(),
                        imageUrl = if (nextEpisode.stillPath != null) TmdbService.stillImageUrl(nextEpisode.stillPath) else null)

                    db.nextEpisodeQueries.lastInsertRowId().executeAsOne()
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

    fun printAllShows() {
        val myShows = db.myShowQueries.selectAll { id, imdbId, tmdbId, tvdbId, facebookId, instagramId, twitterId, name, overview, year, imageUrl, isEnded, nextEpisodeId, episodeId, _, episodeName, episodeNumber, seasonNumber, air_date, episodeImageUrl ->
            "$id. $name($year), Ended: $isEnded, NextEpisodeId: $nextEpisodeId, episodeId: $episodeId, poster: $imageUrl, episodeImage: $episodeImageUrl"
        }.executeAsList()

        log("My Shows: ${myShows.joinToString("\n")}")
    }

    fun upcomingShows(): List<MyShowsListItem.UpcomingShowViewModel> {
        return db.myShowQueries.upcomingShows { id, name, overview, episodeName, episodeNumber, seasonNumber, air_date, imageUrl ->
            val show = MyShowsListItem.UpcomingShowViewModel(
                name = name,
                backdropUrl = imageUrl,
                episodeName = episodeName,
                episodeNumber = "S${seasonNumber}E$episodeNumber",
                timeLeft = "?")
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
