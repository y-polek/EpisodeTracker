package dev.polek.episodetracker.common.datasource.themoviedb.entities

import dev.polek.episodetracker.common.utils.allNotNull
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class GenreEntity(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String)
{
    @Transient val isValid = allNotNull(id, name)
}
