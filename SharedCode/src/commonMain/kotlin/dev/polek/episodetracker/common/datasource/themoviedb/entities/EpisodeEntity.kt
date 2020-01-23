package dev.polek.episodetracker.common.datasource.themoviedb.entities

import dev.polek.episodetracker.common.utils.allNotNull
import dev.polek.episodetracker.common.utils.parseDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class EpisodeEntity(
    @SerialName("id") val tmdbId: Int? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("episode_number") val episodeNumber: Int? = null,
    @SerialName("season_number") val seasonNumber: Int? = null,
    @SerialName("air_date") val airDate: String? = null,
    @SerialName("still_path") val stillPath: String? = null)
{
    @Transient val isValid = allNotNull(tmdbId, episodeNumber, seasonNumber, airDate)
    @Transient val airDateMillis: Long? = if (airDate != null) parseDate(airDate)?.timestamp else null
}
