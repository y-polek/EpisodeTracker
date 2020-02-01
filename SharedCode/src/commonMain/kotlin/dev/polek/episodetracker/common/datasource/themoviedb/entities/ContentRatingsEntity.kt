package dev.polek.episodetracker.common.datasource.themoviedb.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ContentRatingsEntity(
    @SerialName("results") val ratings: List<ContentRatingEntity>? = null
)
