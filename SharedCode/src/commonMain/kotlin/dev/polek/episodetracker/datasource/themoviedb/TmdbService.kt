package dev.polek.episodetracker.datasource.themoviedb

import dev.polek.episodetracker.THE_MOVIE_DB_API_ACCESS_TOKEN
import dev.polek.episodetracker.common.network.bearer
import dev.polek.episodetracker.datasource.themoviedb.entities.SearchResultPageEntity
import io.ktor.client.HttpClient
import io.ktor.client.features.auth.Auth
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.get
import kotlinx.serialization.json.Json

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
        install(JsonFeature) {
            serializer = KotlinxSerializer(json = Json.nonstrict)
        }
    }

    suspend fun search(query: String, page: Int = 1): SearchResultPageEntity {
        require(page >= 1) { "Page number must be >= 1" }

        return client.get("$BASE_URL/search/tv?query=$query&page=$page")
    }

    companion object {
        private const val BASE_URL = "https://api.themoviedb.org/3"
    }
}
