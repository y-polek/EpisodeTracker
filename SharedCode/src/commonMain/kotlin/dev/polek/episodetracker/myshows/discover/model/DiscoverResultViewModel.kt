package dev.polek.episodetracker.myshows.discover.model

data class DiscoverResultViewModel(
    val name: String,
    val year: Int,
    val network: String,
    val posterUrl: String?,
    val overview: String,
    val isInMyShows: Boolean)
