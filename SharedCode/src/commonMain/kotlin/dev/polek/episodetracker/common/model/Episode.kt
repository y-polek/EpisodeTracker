package dev.polek.episodetracker.common.model

data class Episode(
    val name: String,
    val number: EpisodeNumber,
    val airDateMillis: Long?,
    val imageUrl: String?
)
