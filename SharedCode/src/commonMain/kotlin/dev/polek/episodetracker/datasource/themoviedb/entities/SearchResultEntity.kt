package dev.polek.episodetracker.datasource.themoviedb.entities

import dev.polek.episodetracker.utils.allNotNull
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class SearchResultEntity(
    @SerialName("id") val id: Int? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("overview") val overview: String? = null,
    @SerialName("first_air_date") val firstAirDate: String? = null,
    @SerialName("genre_ids") val genreIds: List<Int>? = null)
{
    @Transient val isValid = allNotNull(id, name)
}
