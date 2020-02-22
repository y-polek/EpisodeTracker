package dev.polek.episodetracker.common.presentation.showdetails.model

data class ShowDetailsViewModel(
    val overview: String,
    val genres: List<String>,
    val homePageUrl: String?,
    val imdbUrl: String?,
    val instagramUsername: String?,
    val facebookProfile: String?,
    val twitterUsername: String?
)
