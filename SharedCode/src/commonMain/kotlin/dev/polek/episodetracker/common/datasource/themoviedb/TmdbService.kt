package dev.polek.episodetracker.common.datasource.themoviedb

import dev.polek.episodetracker.THE_MOVIE_DB_API_ACCESS_TOKEN
import dev.polek.episodetracker.common.network.bearer
import dev.polek.episodetracker.common.datasource.themoviedb.entities.*
import dev.polek.episodetracker.common.datasource.themoviedb.entities.GenresEntity
import dev.polek.episodetracker.common.datasource.themoviedb.entities.ShowEntity
import dev.polek.episodetracker.common.model.DiscoverResult
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

    private var genresMap: Map<Int, String>? = null

    suspend fun search(query: String, page: Int = 1): List<DiscoverResult> {
        require(page >= 1) { "Page number must be >= 1" }

        return client.get<SearchResultPageEntity>("$BASE_URL/search/tv?query=$query&page=$page").results
            .filter(SearchResultEntity::isValid)
            .map {
                DiscoverResult(
                    tmdbId = it.id ?: 0,
                    name = it.name.orEmpty(),
                    posterUrl = if (it.posterPath != null) posterImageUrl(it.posterPath) else null,
                    overview = it.overview.orEmpty(),
                    year = it.firstAirDate?.take(4)?.toIntOrNull(),
                    genres = it.genreIds.orEmpty().mapNotNull { id -> findGenreById(id) })
        }
    }

    suspend fun showDetails(tmdbId: Int): ShowEntity {
        return client.get(urlString = "$BASE_URL/tv/$tmdbId")
    }

    suspend fun externalIds(tmdbId: Int): ExternalIdsEntity {
        return client.get("$BASE_URL/tv/$tmdbId/external_ids")
    }

    private suspend fun findGenreById(id: Int): String? {
        if (genresMap == null) {
            genresMap = client.get<GenresEntity>("$BASE_URL/genre/tv/list").genres.map { it.id to it.name }.toMap()
        }

        return genresMap?.get(id)
    }

    companion object {
        private const val BASE_URL = "https://api.themoviedb.org/3"
        private const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p"

        fun posterImageUrl(path: String): String {
            return "$BASE_IMAGE_URL/w500$path"
        }

        fun stillImageUrl(path: String): String {
            return "$BASE_IMAGE_URL/original$path"
        }
    }
}
