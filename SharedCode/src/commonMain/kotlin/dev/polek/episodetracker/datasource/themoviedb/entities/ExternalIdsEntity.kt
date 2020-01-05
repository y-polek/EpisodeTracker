package dev.polek.episodetracker.datasource.themoviedb.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExternalIdsEntity(
    @SerialName("id") val tmdbId: Int? = null,
    @SerialName("imdb_id") val imdbId: String? = null,
    @SerialName("facebook_id") val facebookId: String? = null,
    @SerialName("instagram_id") val instagramId: String? = null,
    @SerialName("twitter_id") val twitterId: String? = null
)
