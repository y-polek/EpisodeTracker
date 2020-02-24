package dev.polek.episodetracker.common.presentation.showdetails.model

import dev.polek.episodetracker.common.model.Episode
import dev.polek.episodetracker.common.model.EpisodeNumber
import dev.polek.episodetracker.common.utils.formatDate
import dev.polek.episodetracker.common.utils.formatTimeBetween
import io.ktor.util.date.GMTDate

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

    companion object {
        fun map(episodes: List<Episode>): List<EpisodeViewModel> {
            val now = GMTDate()
            return episodes.map { episode ->
                val isAired = episode.airDateMillis != null && episode.airDateMillis < now.timestamp
                val timeLeftToRelease = if (!isAired && episode.airDateMillis != null) {
                    formatTimeBetween(now, GMTDate(episode.airDateMillis)).replace(' ', '\n')
                } else {
                    ""
                }
                EpisodeViewModel(
                    episodeNumber = episode.number.episode,
                    seasonNumber = episode.number.season,
                    name = episode.name,
                    airDate = episode.airDateMillis?.let { formatDate(GMTDate(it)) }.orEmpty(),
                    imageUrl = episode.imageUrl,
                    isWatched = false,
                    isAired = isAired,
                    timeLeftToRelease = timeLeftToRelease)
            }
        }
    }
}
