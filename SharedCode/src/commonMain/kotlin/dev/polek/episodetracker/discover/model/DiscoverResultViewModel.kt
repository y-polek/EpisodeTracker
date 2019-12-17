package dev.polek.episodetracker.discover.model

data class DiscoverResultViewModel(
    val id: Int,
    val name: String,
    val year: Int?,
    val posterUrl: String?,
    val overview: String,
    val genres: List<String>,
    val isInMyShows: Boolean)
