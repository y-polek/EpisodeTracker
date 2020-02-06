package dev.polek.episodetracker.common.repositories

import dev.polek.episodetracker.common.presentation.towatch.ToWatchShowViewModel
import dev.polek.episodetracker.common.utils.formatEpisodeNumber
import dev.polek.episodetracker.db.Database

class ToWatchRepository(private val db: Database) {

    fun toWatchShows(): List<ToWatchShowViewModel> {
        return db.myShowQueries.toWatchShows(mapper = ::mapToWatchShow).executeAsList()
    }

    fun toWatchShow(tmdbId: Int): ToWatchShowViewModel? {
        return db.myShowQueries.toWatchShow(tmdbId = tmdbId, mapper = ::mapToWatchShow).executeAsList().firstOrNull()
    }

    fun markNextEpisodeWatched(showTmdbId: Int) {
        db.transaction {
            val nextEpisode = db.episodeQueries.nextNotWatchedEpisode(showTmdbId)
                .executeAsOneOrNull()
                ?: return@transaction

            db.episodeQueries.setEpisodeWatched(
                showTmdbId = showTmdbId,
                seasonNumber = nextEpisode.seasonNumber,
                episodeNumber = nextEpisode.episodeNumber,
                isWatched = true)
        }
    }

    companion object {
        fun mapToWatchShow(
            showTmdbId: Int,
            showName: String,
            seasonNumber: Int,
            episodeNumber: Int,
            episodeName: String,
            episodeImageUrl: String?,
            notWatchedEpisodesCount: Long): ToWatchShowViewModel
        {
            return ToWatchShowViewModel(
                id = showTmdbId,
                name = showName,
                episodeNumber = formatEpisodeNumber(season = seasonNumber, episode = episodeNumber),
                episodeName = episodeName,
                episodeCount = notWatchedEpisodesCount.toInt(),
                imageUrl = episodeImageUrl)
        }
    }
}
