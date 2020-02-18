package dev.polek.episodetracker.common.repositories

import dev.polek.episodetracker.common.datasource.themoviedb.TmdbService
import dev.polek.episodetracker.common.datasource.themoviedb.entities.ShowDetailsEntity

class ShowRepository(private val tmdbService: TmdbService) {

    suspend fun showDetails(showTmdbId: Int): ShowDetailsEntity {
        return tmdbService.showDetails(showTmdbId)
    }
}
