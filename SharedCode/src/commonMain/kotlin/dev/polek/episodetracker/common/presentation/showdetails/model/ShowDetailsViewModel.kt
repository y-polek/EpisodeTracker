package dev.polek.episodetracker.common.presentation.showdetails.model

data class ShowDetailsViewModel(
    val overview: String,
    val contentRating: String,
    val genres: List<String>,
    val airTime: String,
    val duration: String
)
