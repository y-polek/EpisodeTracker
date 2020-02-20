package dev.polek.episodetracker.common.repositories

import dev.polek.episodetracker.common.datasource.omdb.OmdbService
import dev.polek.episodetracker.common.datasource.themoviedb.TmdbService
import dev.polek.episodetracker.common.datasource.themoviedb.entities.ShowDetailsEntity

class ShowRepository(
    private val tmdbService: TmdbService,
    private val omdbService: OmdbService) {

    suspend fun showDetails(showTmdbId: Int): ShowDetailsEntity {
        return tmdbService.showDetails(showTmdbId)
    }

    suspend fun imdbRating(imdbId: String): Float? {
        return omdbService.show(imdbId).imdbRating
    }
}
