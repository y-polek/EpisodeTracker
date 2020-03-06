package dev.polek.episodetracker.common.repositories

import co.touchlab.stately.ensureNeverFrozen
import com.squareup.sqldelight.Query
import dev.polek.episodetracker.common.datasource.db.QueryListener
import dev.polek.episodetracker.common.datasource.db.QueryListener.Subscriber
import dev.polek.episodetracker.common.model.EpisodeNumber
import dev.polek.episodetracker.common.model.ToWatchShow
import dev.polek.episodetracker.db.Database

class ToWatchRepository(private val db: Database) {

    private var toWatchShowsQueryListener: QueryListener<ToWatchShow, List<ToWatchShow>>? = null

    init {
        ensureNeverFrozen()
    }

    fun setToWatchShowsSubscriber(subscriber: Subscriber<List<ToWatchShow>>) {
        removeToWatchShowsSubscriber()

        toWatchShowsQueryListener = QueryListener(
            query = db.myShowQueries.toWatchShows(mapper = ::mapToWatchShow),
            subscriber = subscriber,
            notifyImmediately = true,
            extractQueryResult = Query<ToWatchShow>::executeAsList)
    }

    fun removeToWatchShowsSubscriber() {
        toWatchShowsQueryListener?.destroy()
        toWatchShowsQueryListener = null
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
