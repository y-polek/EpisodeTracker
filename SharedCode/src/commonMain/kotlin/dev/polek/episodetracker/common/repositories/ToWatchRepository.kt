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


        val toWatchShows = db.myShowQueries.toWatchShows { showId, showName, seasonNumber, episodeNumber, episodeName, episodeImageUrl, notWatchedEpisodesCount ->
            ToWatchShowViewModel(
                name = showName,
                episodeNumber = "S$seasonNumber E$episodeNumber",
                episodeName = episodeName,
                episodeCount = notWatchedEpisodesCount.toInt(),
                imageUrl = episodeImageUrl)
        }.executeAsList()
        log("ToWatch Shows: $toWatchShows")

        return toWatchShows
    }
}
