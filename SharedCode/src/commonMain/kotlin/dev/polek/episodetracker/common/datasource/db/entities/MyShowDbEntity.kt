package dev.polek.episodetracker.common.datasource.db.entities

data class MyShowDbEntity(
    val name: String,
    val nextEpisode: NextEpisodeDbEntity?
)
