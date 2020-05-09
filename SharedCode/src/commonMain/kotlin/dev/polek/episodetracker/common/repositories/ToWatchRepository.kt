package dev.polek.episodetracker.common.repositories

import co.touchlab.stately.ensureNeverFrozen
import com.squareup.sqldelight.Query
import dev.polek.episodetracker.common.datasource.db.MultiQueryListener
import dev.polek.episodetracker.common.datasource.db.QueryListener.Subscriber
import dev.polek.episodetracker.common.model.EpisodeNumber
import dev.polek.episodetracker.common.model.ToWatchShow
import dev.polek.episodetracker.db.Database

class ToWatchRepository(private val db: Database) {

    private var toWatchShowsQueryListener: MultiQueryListener<ToWatchShow, ToWatchShow>? = null

    init {
        ensureNeverFrozen()
    }

    fun setToWatchShowsSubscriber(subscriber: Subscriber<List<ToWatchShow>>) {
        removeToWatchShowsSubscriber()

        val showsQuery = db.myShowQueries.toWatchShows(mapper = ::mapToWatchShow)
        val specialsQuery = db.myShowQueries.toWatchSpecials(mapper = ::mapToWatchShow)

        toWatchShowsQueryListener = MultiQueryListener(
            queries = listOf(showsQuery, specialsQuery),
            subscriber = subscriber,
            notifyImmediately = true,
            extractQueryResult = Query<ToWatchShow>::executeAsList)
    }

    fun removeToWatchShowsSubscriber() {
        toWatchShowsQueryListener?.destroy()
        toWatchShowsQueryListener = null
    }

    companion object {
        fun mapToWatchShow(
            showTmdbId: Int,
            showName: String,
            seasonNumber: Int,
            episodeNumber: Int,
            episodeName: String,
            episodeImageUrl: String?,
            notWatchedEpisodesCount: Long): ToWatchShow
        {
            return ToWatchShow(
                tmdbId = showTmdbId,
                name = showName,
                nextEpisodeNumber = EpisodeNumber(season = seasonNumber, episode = episodeNumber),
                nextEpisodeName = episodeName,
                episodeCount = notWatchedEpisodesCount.toInt(),
                imageUrl = episodeImageUrl)
        }
    }
}
