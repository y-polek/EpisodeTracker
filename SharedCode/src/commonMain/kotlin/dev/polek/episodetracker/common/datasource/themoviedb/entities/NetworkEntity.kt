package dev.polek.episodetracker.common.datasource.themoviedb.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class NetworkEntity(
    @SerialName("name") val name: String? = null)
{
    @Transient val isValid = name != null && name.isNotBlank()
}
