package dev.polek.episodetracker.common.datasource.omdb.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class OmdbShowEntity(
    @SerialName("imdbRating") private val imdbRatingText: String? = null)
{
    @Transient val imdbRating: Float? = imdbRatingText?.toFloatOrNull()
}
