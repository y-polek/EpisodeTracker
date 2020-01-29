package dev.polek.episodetracker.common.datasource.themoviedb.entities

import dev.polek.episodetracker.common.utils.allNotNull
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class ShowEntity(
    @SerialName("id") val tmdbId: Int? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("first_air_date") val firstAirDate: String? = null,
    @SerialName("genres") val genres: List<GenreEntity>? = null,
    @SerialName("networks") val network: List<NetworkEntity>? = null,
    @SerialName("overview") val overview: String? = null,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("backdrop_path") val backdropPath: String? = null,
    @SerialName("in_production") val inProduction: Boolean = true,
    @SerialName("next_episode_to_air") val nextEpisodeToAir: EpisodeEntity? = null,
    @SerialName("number_of_seasons") val numberOfSeasons: Int = 1,
    @SerialName("external_ids") val externalIds: ExternalIdsEntity? = null)
{
    @Transient val isValid = allNotNull(tmdbId, name, numberOfSeasons)
    @Transient val year: Int? = firstAirDate?.take(4)?.toIntOrNull()
    @Transient val isEnded = !inProduction
}
