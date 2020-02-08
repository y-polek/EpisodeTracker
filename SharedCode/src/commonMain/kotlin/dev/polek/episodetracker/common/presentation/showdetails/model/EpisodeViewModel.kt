package dev.polek.episodetracker.common.presentation.showdetails.model

import dev.polek.episodetracker.common.model.EpisodeNumber

class EpisodeViewModel(
    episodeNumber: Int,
    seasonNumber: Int,
    val name: String,
    val airDate: String,
    val imageUrl: String?,
    var isWatched: Boolean,
    val isAired: Boolean,
    val timeLeftToRelease: String)
{
    val number = EpisodeNumber(season = seasonNumber, episode = episodeNumber)
}
