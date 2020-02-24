package dev.polek.episodetracker.common.datasource.themoviedb

import dev.polek.episodetracker.THE_MOVIE_DB_API_ACCESS_TOKEN
import dev.polek.episodetracker.common.datasource.themoviedb.entities.*
import dev.polek.episodetracker.common.model.DiscoverResult
import dev.polek.episodetracker.common.network.bearer
import io.ktor.client.HttpClient
import io.ktor.client.features.auth.Auth
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.get
import kotlinx.serialization.json.Json

class TmdbService(client: HttpClient?) {

    private val client = client ?: HttpClient {
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

    constructor() : this(client = null)

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

    suspend fun show(tmdbId: Int): ShowEntity {
        return client.get(urlString = "$BASE_URL/tv/$tmdbId?append_to_response=external_ids,content_ratings")
    }

    suspend fun season(showTmdbId: Int, number: Int): SeasonEntity {
        return client.get(urlString = "$BASE_URL/tv/$showTmdbId/season/$number")
    }

    suspend fun showDetails(showTmdbId: Int): ShowDetailsEntity {
        return client.get(urlString = "$BASE_URL/tv/$showTmdbId?append_to_response=external_ids,content_ratings,videos,credits,recommendations")
    }

    private suspend fun findGenreById(id: Int): String? {
        if (genresMap == null) {
            genresMap = client.get<GenresEntity>("$BASE_URL/genre/tv/list").genres.map { it.id to it.name }.toMap()
        }

        return genresMap?.get(id)
    }

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3"
        private const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p"

        fun posterImageUrl(path: String): String {
            return "$BASE_IMAGE_URL/w500$path"
        }

        fun backdropImageUrl(path: String): String {
            return "$BASE_IMAGE_URL/original$path"
        }

        fun stillImageUrl(path: String): String {
            return "$BASE_IMAGE_URL/original$path"
        }

        fun networkImageUrl(path: String): String {
            return "$BASE_IMAGE_URL/w300$path"
        }

        fun profileImageUrl(path: String): String {
            return "$BASE_IMAGE_URL/h632$path"
        }
    }
}
