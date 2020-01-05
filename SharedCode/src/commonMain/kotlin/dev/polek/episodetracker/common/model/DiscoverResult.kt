package dev.polek.episodetracker.common.model

data class DiscoverResult(
    val tmdbId: Int,
    val name: String,
    val posterUrl: String?,
    val overview: String,
    val year: Int?,
    val genres: List<String>)
