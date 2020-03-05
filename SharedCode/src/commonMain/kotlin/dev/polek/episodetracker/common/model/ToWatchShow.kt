package dev.polek.episodetracker.common.model

data class ToWatchShow(
    val tmdbId: Int,
    val name: String,
    val nextEpisodeNumber: EpisodeNumber,
    val nextEpisodeName: String,
    val episodeCount: Int,
    val imageUrl: String?
)
