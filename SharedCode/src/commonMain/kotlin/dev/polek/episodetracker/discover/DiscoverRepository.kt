package dev.polek.episodetracker.discover

import dev.polek.episodetracker.datasource.themoviedb.TmdbService
import dev.polek.episodetracker.discover.model.DiscoverResult

class DiscoverRepository(private val tmdbService: TmdbService) {

    suspend fun search(query: String): List<DiscoverResult> {
        return tmdbService.search(query)
    }
}
