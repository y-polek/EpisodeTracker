package dev.polek.episodetracker.common.datasource.themoviedb.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecommendationsEntity(
    @SerialName("results") val results: List<RecommendationEntity>? = null
)
