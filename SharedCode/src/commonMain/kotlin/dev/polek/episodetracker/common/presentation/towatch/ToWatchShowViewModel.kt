package dev.polek.episodetracker.common.presentation.towatch

data class ToWatchShowViewModel(
    val name: String,
    val episodeNumber: String,
    val episodeName: String,
    val episodeCount: Int,
    val imageUrl: String?
)
