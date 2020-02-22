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
    val imdbUrl = imdbId?.let { "https://www.imdb.com/title/$it" }
    val instagramUrl = instagramUsername?.let { "https://www.instagram.com/$it" }
    val facebookUrl = facebookProfile?.let { "https://www.facebook.com/$it" }
    val twitterUrl = twitterUsername?.let { "https://twitter.com/$it" }
}
