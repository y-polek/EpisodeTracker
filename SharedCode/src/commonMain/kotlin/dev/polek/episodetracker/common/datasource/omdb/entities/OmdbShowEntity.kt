package dev.polek.episodetracker.common.datasource.omdb.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OmdbShowEntity(
    @SerialName("imdbRating") val imdbRating: Float? = null
)
