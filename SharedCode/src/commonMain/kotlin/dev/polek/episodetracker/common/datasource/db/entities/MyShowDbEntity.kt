package dev.polek.episodetracker.common.datasource.db.entities

data class MyShowDbEntity(
    val tmdbId: Int,
    val name: String,
    val nextEpisode: NextEpisodeDbEntity?
)
