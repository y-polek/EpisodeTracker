package dev.polek.episodetracker.common.datasource.themoviedb.entities

import dev.polek.episodetracker.common.utils.allNotNull
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class ContentRatingEntity(
    @SerialName("iso_3166_1") val country: String? = null,
    @SerialName("rating") val rating: String? = null)
{
    @Transient val isValid: Boolean = allNotNull(country, rating)
}
