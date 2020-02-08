package dev.polek.episodetracker.common.repositories

import dev.polek.episodetracker.common.model.EpisodeNumber
import dev.polek.episodetracker.common.presentation.showdetails.model.EpisodeViewModel
import dev.polek.episodetracker.common.presentation.showdetails.model.SeasonViewModel
import dev.polek.episodetracker.common.utils.formatDate
import dev.polek.episodetracker.common.utils.formatTimeBetween
import dev.polek.episodetracker.db.Database
import io.ktor.util.date.GMTDate

class EpisodesRepository(private val db: Database) {

    fun allSeasons(showTmdbId: Int): List<SeasonViewModel> {
        val now = GMTDate()
        val allEpisodes = db.episodeQueries.episodes(showTmdbId) { name, seasonNumber, episodeNumber, isWatched, airDateMillis, imageUrl ->
            val isAired = airDateMillis != null && airDateMillis < now.timestamp
            val timeLeftToRelease = if (!isAired && airDateMillis != null) {
                formatTimeBetween(now, GMTDate(airDateMillis)).replace(' ', '\n')
            } else {
                ""
            }
            EpisodeViewModel(
                episodeNumber = episodeNumber,
                seasonNumber = seasonNumber,
                name = name,
                airDate = airDateMillis?.let { formatDate(GMTDate(it)) }.orEmpty(),
                imageUrl = imageUrl,
                isWatched = isWatched,
                isAired = isAired,
                timeLeftToRelease = timeLeftToRelease)
        }.executeAsList()

        return allEpisodes.groupBy { it.number.season }
            .map { (season, episodes) ->
                SeasonViewModel(
                    number = season,
                    episodes = episodes.sortedBy { it.number.episode })
            }
            .sortedBy(SeasonViewModel::number)
    }

    fun setEpisodeWatched(showTmdbId: Int, seasonNumber: Int, episodeNumber: Int, isWatched: Boolean) {
        db.episodeQueries.setEpisodeWatched(
            showTmdbId = showTmdbId,
            seasonNumber = seasonNumber,
            episodeNumber = episodeNumber,
            isWatched = isWatched)
    }

    fun markAllWatchedUpTo(showTmdbId: Int, episodeNumber: EpisodeNumber) {
        db.episodeQueries.markAllWatchedUpTo(
            showTmdbId = showTmdbId,
            season = episodeNumber.season,
            episode = episodeNumber.episode)
    }

    fun markAllWatchedUpToSeason(showTmdbId: Int, season: Int) {
        db.episodeQueries.markAllWatchedUpToSeason(
            showTmdbId = showTmdbId,
            season = season)
    }

    fun setSeasonWatched(showTmdbId: Int, seasonNumber: Int, isWatched: Boolean) {
        db.episodeQueries.setSeasonWatched(
            showTmdbId = showTmdbId,
            seasonNumber = seasonNumber,
            isWatched = isWatched)
    }

    fun firstNotWatchedEpisode(tmdbShowId: Int): EpisodeNumber? {
        return db.episodeQueries.nextNotWatchedEpisode(tmdbShowId) { seasonNumber, episodeNumber ->
            EpisodeNumber(season = seasonNumber, episode = episodeNumber)
        }.executeAsOneOrNull()
    }
}
