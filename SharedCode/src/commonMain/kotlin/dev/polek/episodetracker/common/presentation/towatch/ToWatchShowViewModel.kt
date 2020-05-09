package dev.polek.episodetracker.common.presentation.towatch

import dev.polek.episodetracker.common.model.ToWatchShow
import dev.polek.episodetracker.common.utils.formatEpisodeNumber

data class ToWatchShowViewModel(
    val id: Int,
    val name: String,
    val isSpecials: Boolean,
    val episodeNumber: String,
    val episodeName: String,
    val episodeCount: Int,
    val imageUrl: String?)
{
    companion object {
        fun map(show: ToWatchShow): ToWatchShowViewModel {
            val isSpecial = show.nextEpisodeNumber.season == 0
            val episodeNumber = if (isSpecial) {
                formatEpisodeNumber(show.nextEpisodeNumber.episode)
            } else {
                formatEpisodeNumber(show.nextEpisodeNumber)
            }

            return ToWatchShowViewModel(
                id = show.tmdbId,
                name = show.name,
                isSpecials = isSpecial,
                episodeNumber = episodeNumber,
                episodeName = show.nextEpisodeName,
                episodeCount = show.episodeCount,
                imageUrl = show.imageUrl)
        }
    }
}
