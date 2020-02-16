package dev.polek.episodetracker.common.datasource.themoviedb.entities

import dev.polek.episodetracker.common.utils.allNotNull
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class CastMemberEntity(
    @SerialName("name") val name: String? = null,
    @SerialName("character") val character: String? = null,
    @SerialName("profile_path") val profilePath: String? = null)
{
    @Transient val isValid = allNotNull(name, character)
}
