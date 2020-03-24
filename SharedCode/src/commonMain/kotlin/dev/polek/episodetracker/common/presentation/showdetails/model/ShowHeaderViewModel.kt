package dev.polek.episodetracker.common.presentation.showdetails.model

class ShowHeaderViewModel(
    val name: String,
    val imageUrl: String?,
    val rating: String,
    year: Int?,
    endYear: Int?,
    networks: List<String>)
{
    val subhead: String

    init {
        val yearsText = when {
            year != null && endYear != null -> "$year - $endYear"
            year != null -> "$year"
            else -> null
        }
        subhead = when {
            yearsText != null && networks.isNotEmpty() -> "$yearsText ∙ ${networks.joinToString(", ")}"
            yearsText != null -> yearsText
            else -> ""
        }
    }
}
