package dev.polek.episodetracker.common.repositories

import dev.polek.episodetracker.common.datasource.themoviedb.TmdbService
import dev.polek.episodetracker.common.di.Inject
import dev.polek.episodetracker.common.model.DiscoverResult

class DiscoverRepository @Inject constructor(private val tmdbService: TmdbService) {

    suspend fun search(query: String): List<DiscoverResult> {
        return tmdbService.search(query)
    }
}
