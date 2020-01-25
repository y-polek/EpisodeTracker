package dev.polek.episodetracker.common.repositories

import dev.polek.episodetracker.common.logging.log
import dev.polek.episodetracker.common.presentation.towatch.ToWatchShowViewModel
import dev.polek.episodetracker.db.Database
import io.ktor.util.date.GMTDate

class ToWatchRepository(private val db: Database) {

    fun toWatchShows(): List<ToWatchShowViewModel> {

        val episodes = db.episodeQueries.allEpisodes { name, seasonNumber, episodeNumber, isWatched, airDateMillis ->
            "S$seasonNumber E$episodeNumber $name, watched: $isWatched, ${airDateMillis?.let(::GMTDate)}"
        }.executeAsList()
        log("Episodes: ${episodes.joinToString(separator = "\n")}")

        val toWatchShows = db.myShowQueries.toWatchShows(mapper = ::mapToWatchShow).executeAsList()
        log("ToWatch Shows: $toWatchShows")

        return toWatchShows
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
                episodeNumber = "S$seasonNumber E$episodeNumber",
                episodeName = episodeName,
                episodeCount = notWatchedEpisodesCount.toInt(),
                imageUrl = episodeImageUrl)
        }
    }
}
