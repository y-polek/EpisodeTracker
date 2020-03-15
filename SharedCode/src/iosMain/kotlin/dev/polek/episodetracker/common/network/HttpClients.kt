package dev.polek.episodetracker.common.network

import dev.polek.episodetracker.common.logging.log
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.ios.Ios
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import platform.Foundation.NSURLRequestUseProtocolCachePolicy

actual fun platformHttpClient(setup: HttpClientConfig<*>.() -> Unit) = HttpClient(Ios) {
    setup()

    engine {
        configureRequest {
            setCachePolicy(NSURLRequestUseProtocolCachePolicy)
        }
    }

    install(Logging) {
        level = LogLevel.ALL
        logger = object : Logger {
            override fun log(message: String) {
                log("Ktor") { message }
            }
        }
    }
}
