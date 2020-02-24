package dev.polek.episodetracker.common.model

data class Season(
    val name: String,
    val number: Int,
    val episodes: List<Episode>
)
