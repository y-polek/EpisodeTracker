package dev.polek.episodetracker.common.datasource.db.entities

data class NextEpisodeDbEntity(
    val name: String,
    val episodeNumber: Int,
    val seasonNumber: Int,
    val airDateMillis: Long?
)
