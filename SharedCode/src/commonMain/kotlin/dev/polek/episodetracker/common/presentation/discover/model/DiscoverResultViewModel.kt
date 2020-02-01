package dev.polek.episodetracker.common.presentation.discover.model

data class DiscoverResultViewModel(
    val id: Int,
    val name: String,
    val year: Int?,
    val posterUrl: String?,
    val overview: String,
    val genres: List<String>,
    var isInMyShows: Boolean,
    var isAddInProgress: Boolean = false)
