package dev.polek.episodetracker.common.datasource.themoviedb.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConfigurationEntity(@SerialName("images") val images: Images)
{
    @Serializable
    data class Images(
        @SerialName("secure_base_url") val baseUrl: String,
        @SerialName("poster_sizes") val posterSizes: List<String>,
        @SerialName("backdrop_sizes") val backdropSizes: List<String>,
        @SerialName("logo_sizes") val logoSizes: List<String>,
        @SerialName("profile_sizes") val profileSizes: List<String>,
        @SerialName("still_sizes") val stillSizes: List<String>
    )
}
