package dev.polek.episodetracker.datasource.db.entities

data class MyShowDbEntity(
    val name: String,
    val nextEpisode: NextEpisodeDbEntity?
)
