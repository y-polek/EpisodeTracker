package dev.polek.episodetracker.common.presentation.showdetails.model

class ShowHeaderViewModel(
    val name: String,
    val imageUrl: String?,
    val rating: String,
    year: Int?,
    endYear: Int?,
    network: String?)
{
    val subhead: String

    init {
        val yearsText = when {
            year != null && endYear != null -> "$year - $endYear"
            year != null -> "$year"
            else -> null
        }
        subhead = when {
            yearsText != null && network != null -> "$yearsText ∙ $network"
            yearsText != null -> "$yearsText - $endYear"
            else -> ""
        }
    }
}
