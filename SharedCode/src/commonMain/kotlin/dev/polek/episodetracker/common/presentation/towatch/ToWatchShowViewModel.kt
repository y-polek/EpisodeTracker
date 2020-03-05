package dev.polek.episodetracker.common.presentation.towatch

import dev.polek.episodetracker.common.model.ToWatchShow
import dev.polek.episodetracker.common.utils.formatEpisodeNumber

data class ToWatchShowViewModel(
    val id: Int,
    val name: String,
    val episodeNumber: String,
    val episodeName: String,
    val episodeCount: Int,
    val imageUrl: String?)
{
    companion object {
        fun map(show: ToWatchShow): ToWatchShowViewModel {
            return ToWatchShowViewModel(
                id = show.tmdbId,
                name = show.name,
                episodeNumber = formatEpisodeNumber(show.nextEpisodeNumber),
                episodeName = show.nextEpisodeName,
                episodeCount = show.episodeCount,
                imageUrl = show.imageUrl)
        }
    }
}
