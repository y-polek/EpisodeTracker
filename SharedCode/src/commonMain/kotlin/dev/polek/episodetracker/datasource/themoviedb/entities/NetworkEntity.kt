package dev.polek.episodetracker.datasource.themoviedb.entities

import dev.polek.episodetracker.utils.allNotNull
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
internal data class NetworkEntity(
    @SerialName("name") val name: String? = null,
    @SerialName("logo_path") val logoPath: String? = null)
{
    @Transient val isValid = allNotNull(name)
}
