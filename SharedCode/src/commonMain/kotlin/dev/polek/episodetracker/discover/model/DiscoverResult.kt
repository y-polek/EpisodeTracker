package dev.polek.episodetracker.discover.model

data class DiscoverResult(
    val id: Int,
    val name: String,
    val posterUrl: String?,
    val overview: String,
    val year: Int?,
    val genres: List<String>)
