package dev.polek.episodetracker.common.datasource.themoviedb.entities

import dev.polek.episodetracker.common.utils.allNotNull
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class SeasonEntity(
    @SerialName("id") val tmdbId: Int? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("season_number") val number: Int? = null,
    @SerialName("episodes") val episodes: List<EpisodeEntity>? = null)
{
    @Transient val isValid = allNotNull(tmdbId, number)
}
