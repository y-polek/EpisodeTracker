package dev.polek.episodetracker.common.presentation.showdetails.model

data class ShowDetailsViewModel(
    val overview: String,
    val genres: List<String>,
    val homePageUrl: String?,
    val imdbId: String?,
    val instagramUsername: String?,
    val facebookProfile: String?,
    val twitterUsername: String?)
{
    val imdbUrl: String? = imdbId?.let { "https://www.imdb.com/title/$it" }
}
