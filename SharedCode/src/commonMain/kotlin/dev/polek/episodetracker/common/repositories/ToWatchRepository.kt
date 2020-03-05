package dev.polek.episodetracker.common.repositories

import co.touchlab.stately.ensureNeverFrozen
import com.squareup.sqldelight.Query
import dev.polek.episodetracker.common.datasource.db.QueryListener
import dev.polek.episodetracker.common.datasource.db.QueryListener.Subscriber
import dev.polek.episodetracker.common.presentation.towatch.ToWatchShowViewModel
import dev.polek.episodetracker.common.utils.formatEpisodeNumber
import dev.polek.episodetracker.db.Database

class ToWatchRepository(private val db: Database) {

    private var toWatchShowsQueryListener: QueryListener<ToWatchShowViewModel, List<ToWatchShowViewModel>>? = null

    init {
        ensureNeverFrozen()
    }

    fun toWatchShows(): List<ToWatchShowViewModel> {
        return db.myShowQueries.toWatchShows(mapper = ::mapToWatchShow).executeAsList()
    }

    fun toWatchShowsQuery(): Query<ToWatchShowViewModel> {
        return db.myShowQueries.toWatchShows(mapper = ::mapToWatchShow)
    }

    fun toWatchShow(tmdbId: Int): ToWatchShowViewModel? {
        return db.myShowQueries.toWatchShow(tmdbId = tmdbId, mapper = ::mapToWatchShow).executeAsList().firstOrNull()
    }

    fun setToWatchShowsSubscriber(subscriber: Subscriber<List<ToWatchShowViewModel>>) {
        toWatchShowsQueryListener = QueryListener(
            query = db.myShowQueries.toWatchShows(mapper = ::mapToWatchShow),
            subscriber = subscriber,
            notifyImmediately = true,
            extractQueryResult = Query<ToWatchShowViewModel>::executeAsList)
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
