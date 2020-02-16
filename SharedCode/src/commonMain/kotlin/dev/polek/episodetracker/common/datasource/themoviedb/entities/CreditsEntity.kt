package dev.polek.episodetracker.common.datasource.themoviedb.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreditsEntity(
    @SerialName("cast") val cast: List<CastMemberEntity>? = null
)
