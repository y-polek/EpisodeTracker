package dev.polek.episodetracker.common.presentation.showdetails.model

data class EpisodeViewModel(
    val number: Int,
    val season: Int,
    val name: String,
    val airDate: String,
    val imageUrl: String?,
    var isWatched: Boolean,
    val isAired: Boolean,
    val timeLeftToRelease: String
)
