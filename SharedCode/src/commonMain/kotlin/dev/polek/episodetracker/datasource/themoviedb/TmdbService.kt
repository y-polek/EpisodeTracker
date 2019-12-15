package dev.polek.episodetracker.datasource.themoviedb

import dev.polek.episodetracker.THE_MOVIE_DB_API_ACCESS_TOKEN
import dev.polek.episodetracker.common.network.bearer
import io.ktor.client.HttpClient
import io.ktor.client.features.auth.Auth
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.get

class TmdbService {

    private val client = HttpClient {
        install(Auth) {
            bearer {
                token = THE_MOVIE_DB_API_ACCESS_TOKEN
            }
        }
        install(Logging) {
            level = LogLevel.ALL
        }
    }

    suspend fun search(query: String, page: Int = 1): String {
        require(page >= 1) { "Page number must be >= 1" }

        return client.get<String>("$BASE_URL/search/tv?query=$query&page=$page")
    }

    companion object {
        private const val BASE_URL = "https://api.themoviedb.org/3"
    }
}
