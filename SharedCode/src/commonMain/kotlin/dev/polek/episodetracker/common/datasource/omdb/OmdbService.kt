package dev.polek.episodetracker.common.datasource.omdb

import dev.polek.episodetracker.OMDB_API_KEY
import dev.polek.episodetracker.common.datasource.omdb.entities.OmdbShowEntity
import dev.polek.episodetracker.common.network.platformHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import kotlinx.serialization.json.Json

class OmdbService(client: HttpClient?) {

    private val client = client ?: platformHttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer(json = Json.nonstrict)
        }
    }

    constructor() : this(client = null)

    suspend fun show(imdbId: String): OmdbShowEntity {
        return client.get(urlString = "$BASE_URL&i=$imdbId")
    }

    companion object {
        const val BASE_URL = "https://omdbapi.com/?apikey=$OMDB_API_KEY"
    }
}
