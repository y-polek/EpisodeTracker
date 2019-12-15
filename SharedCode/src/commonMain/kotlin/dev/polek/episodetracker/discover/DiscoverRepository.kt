package dev.polek.episodetracker.discover

import dev.polek.episodetracker.datasource.themoviedb.TmdbService

class DiscoverRepository(private val tmdbService: TmdbService) {

    suspend fun search(query: String) {
        val result = tmdbService.search(query)
        println("Result: $result")
    }
}
