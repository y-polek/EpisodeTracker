package dev.polek.episodetracker.datasource.themoviedb.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenreEntity(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String
)
