package dev.polek.episodetracker.common.presentation.showdetails.model

class RecommendationViewModel(
    val showId: Int,
    val name: String,
    val imageUrl: String?,
    year: Int?,
    networks: List<String>)
{
    val subhead: String = when {
        year != null && networks.isNotEmpty() -> "$year ∙ ${networks.joinToString(", ")}"
        year != null -> "$year"
        networks.isNotEmpty() -> networks.joinToString(", ")
        else -> ""
    }
}
