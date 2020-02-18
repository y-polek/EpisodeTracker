package dev.polek.episodetracker.common.datasource.themoviedb.entities

import dev.polek.episodetracker.common.utils.allNotNull
import dev.polek.episodetracker.common.utils.parseDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class RecommendationEntity(
    @SerialName("id") val tmdbId: Int? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("first_air_date") val firstAirDate: String? = null,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("backdrop_path") val backdropPath: String? = null,
    @SerialName("networks") val networks: List<NetworkEntity>? = null)
{
    @Transient val isValid = allNotNull(tmdbId, name)
    @Transient val year: Int? = firstAirDate?.let(::parseDate)?.year
    @Transient val network: NetworkEntity? = networks?.firstOrNull(NetworkEntity::isValid)
}
