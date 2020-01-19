package dev.polek.episodetracker.towatch

data class ToWatchShowViewModel(
    val name: String,
    val backdropUrl: String?,
    val episodeNumber: String,
    val episodeName: String,
    val episodeCount: Int
)
