package dev.polek.episodetracker.common.repositories

import co.touchlab.stately.ensureNeverFrozen
import com.squareup.sqldelight.Query
import dev.polek.episodetracker.common.datasource.db.MultiQueryListener
import dev.polek.episodetracker.common.datasource.db.QueryListener
import dev.polek.episodetracker.common.datasource.db.QueryListener.Subscriber
import dev.polek.episodetracker.common.model.EpisodeNumber
import dev.polek.episodetracker.common.model.ToWatchShow
import dev.polek.episodetracker.common.preferences.Preferences
import dev.polek.episodetracker.db.Database

class ToWatchRepository(
    private val db: Database,
    private val preferences: Preferences)
{
    private var toWatchShowsQueryListener: MultiQueryListener<ToWatchShow, ToWatchShow>? = null
    private var numberOfToWatchEpisodesQueryListener: QueryListener<Long, Long>? = null

    init {
        ensureNeverFrozen()
    }

    fun setToWatchShowsSubscriber(subscriber: Subscriber<List<ToWatchShow>>) {
        removeToWatchShowsSubscriber()

        val showsQuery = db.myShowQueries.toWatchShows(mapper = ::mapToWatchShow)
        val specialsQuery = db.myShowQueries.toWatchSpecials(mapper = ::mapToWatchShow)
        val queries = if (preferences.showSpecialsInToWatch) {
            listOf(showsQuery, specialsQuery)
        } else {
            listOf(showsQuery)
        }

        toWatchShowsQueryListener = MultiQueryListener(
            queries = queries,
            subscriber = subscriber,
            notifyImmediately = true,
            extractQueryResult = Query<ToWatchShow>::executeAsList)
    }

    fun removeToWatchShowsSubscriber() {
        toWatchShowsQueryListener?.destroy()
        toWatchShowsQueryListener = null
    }

    fun setNumberOfToWatchEpisodesSubscriber(subscriber: Subscriber<Long>) {
        removeNumberOfToWatchEpisodesSubscriber()

        val query = if (preferences.showSpecialsInToWatch) {
            db.episodeQueries.numberOfNotWatchedEpisodes()
        } else {
            db.episodeQueries.numberOfNotWatchedEpisodesExcludingSpecials()
        }

        numberOfToWatchEpisodesQueryListener = QueryListener(
            query = query,
            subscriber = subscriber,
            notifyImmediately = true,
            extractQueryResult = Query<Long>::executeAsOne)
    }

    fun removeNumberOfToWatchEpisodesSubscriber() {
        numberOfToWatchEpisodesQueryListener?.destroy()
        numberOfToWatchEpisodesQueryListener = null
    }

    companion object {
        fun mapToWatchShow(
            showTmdbId: Int,
            showName: String,
            showImageUrl: String?,
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
                imageUrl = episodeImageUrl ?: showImageUrl)
        }
    }
}
