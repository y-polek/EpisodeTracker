package dev.polek.episodetracker.common.network

import io.ktor.client.features.auth.Auth
import io.ktor.client.features.auth.AuthProvider
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.HttpHeaders
import io.ktor.http.auth.HttpAuthHeader

fun Auth.bearer(block: BearerAuthConfig.() -> Unit) {
    with(BearerAuthConfig().apply(block)) {
        providers.add(BearerAuthProvider(token, sendWithoutRequest))
    }
}

class BearerAuthConfig {
    lateinit var token: String
    var sendWithoutRequest: Boolean = true
}

class BearerAuthProvider(
    private val token: String,
    override val sendWithoutRequest: Boolean): AuthProvider
{
    override fun isApplicable(auth: HttpAuthHeader) = true

    override suspend fun addRequestHeaders(request: HttpRequestBuilder) {
        request.headers[HttpHeaders.Authorization] = "Bearer $token"
    }
}
