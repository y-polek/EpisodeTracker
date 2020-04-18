package dev.polek.episodetracker.common.presentation.persondetails

import dev.polek.episodetracker.common.presentation.showdetails.model.RecommendationViewModel
import dev.polek.episodetracker.common.utils.formatDate
import dev.polek.episodetracker.common.utils.yearsSince
import io.ktor.util.date.GMTDate

class PersonViewModel(
    val id: Int,
    val name: String,
    val biography: String?,
    val imageUrl: String?,
    birthDate: GMTDate?,
    deathDate: GMTDate?,
    val birthPlace: String?,
    val shows: List<RecommendationViewModel>,
    val homePageUrl: String?,
    val imdbId: String?,
    val instagramUsername: String?,
    val facebookProfile: String?,
    val twitterUsername: String?)
{
    val imdbUrl = imdbId?.let { "https://www.imdb.com/name/$it" }
    val instagramUrl = instagramUsername?.let { "https://www.instagram.com/$it" }
    val facebookUrl = facebookProfile?.let { "https://www.facebook.com/$it" }
    val twitterUrl = twitterUsername?.let { "https://twitter.com/$it" }

    val dates: String?

    init {
        dates = when {
            birthDate == null -> null
            deathDate == null -> "${formatDate(birthDate)} (age ${yearsSince(birthDate)})"
            else -> "${birthDate.year} - ${deathDate.year}"
        }
    }
}
