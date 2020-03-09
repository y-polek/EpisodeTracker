package dev.polek.episodetracker.common.network

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig

expect fun platformHttpClient(setup: HttpClientConfig<*>.() -> Unit): HttpClient
