package dev.polek.episodetracker.common.presentation.towatch

import dev.polek.episodetracker.common.model.EpisodeNumber
import dev.polek.episodetracker.common.model.ToWatchShow
import dev.polek.episodetracker.common.utils.formatEpisodeNumber

data class ToWatchShowViewModel(
    val id: Int,
    val name: String,
    val isSpecials: Boolean,
    val nextEpisodeNumber: EpisodeNumber,
    val nextEpisodeName: String,
    val episodeCount: Int,
    val imageUrl: String?)
{
    val nextEpisodeNumberText: String = if (isSpecials) {
        formatEpisodeNumber(this.nextEpisodeNumber.episode)
    } else {
        formatEpisodeNumber(this.nextEpisodeNumber)
    }

    companion object {
        fun map(show: ToWatchShow): ToWatchShowViewModel {
            return ToWatchShowViewModel(
                id = show.tmdbId,
                name = show.name,
                isSpecials = show.nextEpisodeNumber.season == 0,
                nextEpisodeNumber = show.nextEpisodeNumber,
                nextEpisodeName = show.nextEpisodeName,
                episodeCount = show.episodeCount,
                imageUrl = show.imageUrl)
        }
    }
}
