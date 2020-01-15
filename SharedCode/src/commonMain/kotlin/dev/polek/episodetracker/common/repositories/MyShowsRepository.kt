package dev.polek.episodetracker.common.repositories

import dev.polek.episodetracker.common.logging.log
import dev.polek.episodetracker.datasource.themoviedb.TmdbService
import dev.polek.episodetracker.db.Database

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
                isEnded = show.isEnded,
                isUpcoming = show.isUpcoming,
                nextEpisodeId = nextEpisodeId)
        }






        val myShows = db.myShowQueries.selectAll { id, imdbId, _, tvdbId, facebookId, instagramId, twitterId, name, overview, year, isEnded, isUpcoming, nextEpisodeId ->
            "$id. $name ($year), Ended: $isEnded, Upcoming: $isUpcoming, Next episode: $nextEpisodeId"
        }.executeAsList()
        log("My Shows: $myShows")
    }

    suspend fun removeShow(tmdbId: Int) {
        db.myShowQueries.deleteByTmdbId(tmdbId)
    }

    suspend fun isInMyShows(tmdbId: Int): Boolean {
        return db.myShowQueries.isInMyShows(tmdbId).executeAsOne()
    }
}
