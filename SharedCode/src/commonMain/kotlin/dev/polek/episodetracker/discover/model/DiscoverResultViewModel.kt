package dev.polek.episodetracker.discover.model

data class DiscoverResultViewModel(
    val name: String,
    val year: Int,
    val network: String,
    val posterUrl: String?,
    val overview: String,
    val isInMyShows: Boolean)
