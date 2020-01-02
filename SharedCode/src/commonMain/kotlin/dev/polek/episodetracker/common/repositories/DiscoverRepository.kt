package dev.polek.episodetracker.common.repositories

import dev.polek.episodetracker.datasource.themoviedb.TmdbService
import dev.polek.episodetracker.discover.model.DiscoverResult

class DiscoverRepository(private val tmdbService: TmdbService) {

    suspend fun search(query: String): List<DiscoverResult> {
        return tmdbService.search(query)
    }
}
