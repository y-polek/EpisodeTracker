package dev.polek.episodetracker.common.presentation.showdetails.model

data class RecommendationViewModel(
    val showId: Int,
    val name: String,
    val overview: String,
    val imageUrl: String?,
    val year: Int?,
    val network: String?
)
