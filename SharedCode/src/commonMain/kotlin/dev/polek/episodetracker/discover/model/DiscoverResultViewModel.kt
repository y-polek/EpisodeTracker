package dev.polek.episodetracker.discover.model

data class DiscoverResultViewModel(
    val tmdbId: Int,
    val name: String,
    val year: Int?,
    val posterUrl: String?,
    val overview: String,
    val genres: List<String>,
    val isInMyShows: Boolean)
