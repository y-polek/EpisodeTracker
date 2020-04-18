package dev.polek.episodetracker.common.datasource.themoviedb.entities

import dev.polek.episodetracker.common.utils.allNotNull
import dev.polek.episodetracker.common.utils.parseDate
import io.ktor.util.date.GMTDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class PersonDetailsEntity(
    @SerialName("id") val tmdbId: Int? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("birthday") private val birthdayDate: String? = null,
    @SerialName("deathday") private val deathdayDate: String? = null,
    @SerialName("place_of_birth") val placeOfBirth: String? = null,
    @SerialName("biography") val biography: String? = null,
    @SerialName("profile_path") val profilePath: String? = null,
    @SerialName("homepage") val homepage: String? = null,
    @SerialName("external_ids") val externalIds: ExternalIdsEntity? = null,
    @SerialName("tv_credits") private val tvCredits: TvCreditEntity? = null)
{
    @Transient val isValid = allNotNull(tmdbId, name)
    @Transient val birthday: GMTDate? = birthdayDate?.let(::parseDate)
    @Transient val deathday: GMTDate? = deathdayDate?.let(::parseDate)
    @Transient val knownForShows: List<RecommendationEntity> = tvCredits?.cast?.filter(RecommendationEntity::isValid).orEmpty()
}
