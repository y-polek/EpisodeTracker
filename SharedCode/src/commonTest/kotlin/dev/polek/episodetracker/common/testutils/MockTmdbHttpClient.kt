package dev.polek.episodetracker.common.testutils

import dev.polek.episodetracker.common.resources.Resource
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.*
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.HttpResponseData
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.serialization.json.Json

val mockTmdbHttpClient = HttpClient(MockEngine) {
    install(JsonFeature) {
        serializer = KotlinxSerializer(json = Json.nonstrict)
    }
    engine {
        addHandler { request ->
            val url = TmdbUrl.parse(request.url.toString())

            when (url) {
                is TmdbUrl.Show -> respondJson(showDetailsJson(url.showId))
                is TmdbUrl.Season -> respondJson(seasonJson(showId = url.showId, season = url.season))
                else -> respondBadRequest()
            }
        }
    }
}

private fun MockRequestHandleScope.respondJson(json: String?): HttpResponseData {
    if (json == null) return respondError(HttpStatusCode.NotFound)
    return respond(
        content = json,
        headers = headersOf("content-type", "application/json;charset=utf-8")
    )
}

private fun showDetailsJson(showId: Int): String? {
    return Resource.read("show_$showId.json")
}

private fun seasonJson(showId: Int, season: Int): String? {
    return Resource.read("season_${showId}_$season.json")
}
