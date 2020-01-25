package dev.polek.episodetracker.common.presentation.towatch

data class ToWatchShowViewModel(
    val id: Long,
    val name: String,
    val episodeId: Long,
    val episodeNumber: String,
    val episodeName: String,
    val episodeCount: Int,
    val imageUrl: String?
)
