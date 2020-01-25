package dev.polek.episodetracker.common.repositories

import dev.polek.episodetracker.common.presentation.towatch.ToWatchShowViewModel
import dev.polek.episodetracker.common.utils.formatEpisodeNumber
import dev.polek.episodetracker.db.Database

class ToWatchRepository(private val db: Database) {

    fun toWatchShows(): List<ToWatchShowViewModel> {
        return db.myShowQueries.toWatchShows(mapper = ::mapToWatchShow).executeAsList()
    }

    fun toWatchShow(showId: Long): ToWatchShowViewModel? {
        return db.myShowQueries.toWatchShow(id = showId, mapper = ::mapToWatchShow).executeAsList().firstOrNull()
    }

    fun markEpisodeWatched(episodeId: Long) {
        db.episodeQueries.setEpisodeWatched(id = episodeId, isWatched = true)
    }

    companion object {
        fun mapToWatchShow(
            showId: Long?,
            showName: String,
            episodeId: Long?,
            seasonNumber: Int,
            episodeNumber: Int,
            episodeName: String,
            episodeImageUrl: String?,
            notWatchedEpisodesCount: Long): ToWatchShowViewModel
        {
            return ToWatchShowViewModel(
                id = showId!!,
                name = showName,
                episodeId = episodeId!!,
                episodeNumber = formatEpisodeNumber(season = seasonNumber, episode = episodeNumber),
                episodeName = episodeName,
                episodeCount = notWatchedEpisodesCount.toInt(),
                imageUrl = episodeImageUrl)
        }
    }
}
