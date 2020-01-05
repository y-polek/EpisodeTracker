package dev.polek.episodetracker.common.repositories

import dev.polek.episodetracker.common.logging.log
import dev.polek.episodetracker.datasource.themoviedb.TmdbService
import dev.polek.episodetracker.db.Database

class MyShowsRepository(
    private val db: Database,
    private val tmdbService: TmdbService)
{
    suspend fun addShow(tmdbId: Int) {
        val show = tmdbService.showDetails(tmdbId)
        val imdbId = tmdbService.externalIds(tmdbId).imdbId ?: TODO("Support TV Shows without IMDB ID")

        db.myShowQueries.insert(
            imdbId = imdbId,
            tmdbId = tmdbId.toLong(),
            name = show.name.orEmpty(),
            overview = show.overview.orEmpty(),
            year = show.year?.toLong())






        val myShows = db.myShowQueries.selectAll { id, _, _, name, _, year -> "$id. $name ($year)" }.executeAsList()
        log("My Shows: $myShows")
    }
}
