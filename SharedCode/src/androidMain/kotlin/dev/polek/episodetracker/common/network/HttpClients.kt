package dev.polek.episodetracker.common.network

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp
import okhttp3.logging.HttpLoggingInterceptor

actual fun platformHttpClient(setup: HttpClientConfig<*>.() -> Unit) = HttpClient(OkHttp) {
    setup()

    engine {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        addInterceptor(logging)
    }
}
