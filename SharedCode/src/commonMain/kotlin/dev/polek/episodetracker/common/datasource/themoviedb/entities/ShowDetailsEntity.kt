package dev.polek.episodetracker.common.datasource.themoviedb.entities

import dev.polek.episodetracker.common.utils.allNotNull
import dev.polek.episodetracker.common.utils.blankToNull
import dev.polek.episodetracker.common.utils.parseDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class ShowDetailsEntity(
    @SerialName("id") val tmdbId: Int? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("first_air_date") val firstAirDate: String? = null,
    @SerialName("last_air_date") val lastAirDate: String? = null,
    @SerialName("genres") val genreEntities: List<GenreEntity>? = null,
    @SerialName("networks") val networks: List<NetworkEntity>? = null,
    @SerialName("overview") val overview: String? = null,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("backdrop_path") val backdropPath: String? = null,
    @SerialName("homepage") val homepage: String? = null,
    @SerialName("in_production") val inProduction: Boolean = true,
    @SerialName("next_episode_to_air") val nextEpisodeToAir: EpisodeEntity? = null,
    @SerialName("number_of_seasons") val numberOfSeasons: Int = 1,
    @SerialName("external_ids") val externalIds: ExternalIdsEntity? = null,
    @SerialName("content_ratings") val contentRatings: ContentRatingsEntity? = null,
    @SerialName("videos") val videosEntity: VideosEntity? = null,
    @SerialName("credits") val credits: CreditsEntity? = null,
    @SerialName("recommendations") val recommendationsEntity: RecommendationsEntity? = null)
{
    @Transient val isValid = allNotNull(tmdbId, name, numberOfSeasons)
    @Transient val year: Int? = firstAirDate?.let(::parseDate)?.year
    @Transient val lastYear: Int? = lastAirDate?.let(::parseDate)?.year
    @Transient val isEnded = !inProduction
    @Transient val genres: List<String> = genreEntities?.filter(GenreEntity::isValid)?.map(GenreEntity::name).orEmpty()
    @Transient val contentRating: String? = contentRatings?.ratings?.firstOrNull { it.country == "US" }?.rating
    @Transient val network: NetworkEntity? = networks?.firstOrNull(NetworkEntity::isValid)
    @Transient val homePageUrl = homepage?.blankToNull()
    @Transient val videos: List<VideoEntity> = videosEntity?.results?.filter(VideoEntity::isValid).orEmpty()
    @Transient val cast: List<CastMemberEntity> = credits?.cast?.filter(CastMemberEntity::isValid).orEmpty()
    @Transient val recommendations: List<RecommendationEntity> = recommendationsEntity?.results?.filter(RecommendationEntity::isValid).orEmpty()
}
