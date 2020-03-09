package dev.polek.episodetracker.common.network

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging

actual fun platformHttpClient(setup: HttpClientConfig<*>.() -> Unit) = HttpClient(OkHttp) {
    setup()

    install(Logging) {
        level = LogLevel.ALL
    }
}
