package dev.polek.episodetracker.datasource.db.entities

import io.ktor.util.date.GMTDate

data class NextEpisodeDbEntity(
    val name: String,
    val episodeNumber: Int,
    val seasonNumber: Int,
    val airDate: GMTDate,
    val imageUrl: String?
)
