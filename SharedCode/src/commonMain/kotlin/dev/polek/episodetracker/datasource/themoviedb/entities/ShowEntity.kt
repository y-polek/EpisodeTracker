package dev.polek.episodetracker.datasource.themoviedb.entities

import dev.polek.episodetracker.utils.allNotNull
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
internal data class ShowEntity(
    @SerialName("id") val id: Int? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("first_air_date") val firstAirDate: String? = null,
    @SerialName("genres") val genres: List<GenreEntity>? = null,
    @SerialName("networks") val network: List<NetworkEntity>? = null,
    @SerialName("overview") val overview: String? = null,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("backdrop_path") val backdropPath: String? = null)
{
    @Transient val isValid = allNotNull(id, name)
}
