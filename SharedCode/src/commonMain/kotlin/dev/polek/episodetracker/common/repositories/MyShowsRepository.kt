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

        db.myShowQueries.insert(
            imdbId = externalIds.imdbId,
            tmdbId = tmdbId.toLong(),
            tvdbId = externalIds.tvdbId?.toLong(),
            facebookId = externalIds.facebookId,
            instagramId = externalIds.instagramId,
            twitterId = externalIds.twitterId,
            name = show.name.orEmpty(),
            overview = show.overview.orEmpty(),
            year = show.year?.toLong())






        val myShows = db.myShowQueries.selectAll { id, _, _, _, _, _, _, name, _, year -> "$id. $name ($year)" }.executeAsList()
        log("My Shows: $myShows")
    }

    suspend fun removeShow(tmdbId: Int) {
        db.myShowQueries.deleteByTmdbId(tmdbId.toLong())
    }

    suspend fun isInMyShows(tmdbId: Int): Boolean {
        return db.myShowQueries.isInMyShows(tmdbId.toLong()).executeAsOne()
    }
}
