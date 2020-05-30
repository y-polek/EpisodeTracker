package dev.polek.episodetracker.common.repositories

import com.squareup.sqldelight.Query
import dev.polek.episodetracker.common.datasource.db.QueryListener
import dev.polek.episodetracker.common.datasource.db.QueryListener.Subscriber
import dev.polek.episodetracker.common.di.Inject
import dev.polek.episodetracker.common.logging.logw
import dev.polek.episodetracker.common.presentation.myshows.model.MyShowsListItem.ShowViewModel
import dev.polek.episodetracker.common.presentation.myshows.model.MyShowsListItem.UpcomingShowViewModel
import dev.polek.episodetracker.common.utils.formatEpisodeNumber
import dev.polek.episodetracker.common.utils.formatTimeBetween
import dev.polek.episodetracker.common.utils.now
import dev.polek.episodetracker.db.Database
import dev.polek.episodetracker.db.ShowDetails
import io.ktor.util.date.GMTDate

class MyShowsRepository @Inject constructor(
    private val db: Database,
    private val addToMyShowsQueue: AddToMyShowsQueue)
{
    private var lastWeekShowsQueryListener: QueryListener<UpcomingShowViewModel, List<UpcomingShowViewModel>>? = null
    private var upcomingShowsQueryListener: QueryListener<UpcomingShowViewModel, List<UpcomingShowViewModel>>? = null
    private var toBeAnnouncedShowsQueryListener: QueryListener<ShowViewModel, List<ShowViewModel>>? = null
    private var endedShowsQueryListener: QueryListener<ShowViewModel, List<ShowViewModel>>? = null
    private var archivedShowsQueueListener: QueryListener<ShowViewModel, List<ShowViewModel>>? = null
    private var isAddedOrAddingQueryListeners = mutableMapOf<Int, QueryListener<Boolean, Boolean>>()
    private var isArchivedQueryListeners = mutableMapOf<Int, QueryListener<Boolean, Boolean>>()

    fun addShow(tmdbId: Int, markAllEpisodesWatched: Boolean = false, archive: Boolean = false) {
        if (isAddedOrAddingToMyShows(tmdbId)) {
            logw { "Trying to add Show that's already in My Shows" }
            return
        }

        addToMyShowsQueue.addShow(tmdbId, markAllEpisodesWatched, archive)
    }

    fun removeShow(tmdbId: Int) {
        addToMyShowsQueue.cancelAddIfExist(tmdbId)

        db.transaction {
            db.episodeQueries.deleteByTmdbId(tmdbId)
            db.myShowQueries.deleteByTmdbId(tmdbId)
        }
    }

    fun isAddedToMyShows(tmdbId: Int): Boolean {
        return db.myShowQueries.isInMyShows(tmdbId).executeAsOne()
    }

    fun isAddedOrAddingToMyShows(tmdbId: Int): Boolean {
        return db.myShowQueries.isAddedOrAdding(tmdbId).executeAsOne()
    }

    fun isArchived(showTmdbId: Int): Boolean {
        return db.myShowQueries.isArchived(showTmdbId).executeAsOne()
    }

    fun setIsAddedOrAddingToMyShowsSubscriber(showTmdbId: Int, subscriber: Subscriber<Boolean>) {
        removeIsAddedOrAddingToMyShowsSubscriber(showTmdbId)

        isAddedOrAddingQueryListeners[showTmdbId] = QueryListener(
            query = db.myShowQueries.isAddedOrAdding(showTmdbId),
            subscriber = subscriber,
            notifyImmediately = true,
            extractQueryResult = Query<Boolean>::executeAsOne)
    }

    fun removeIsAddedOrAddingToMyShowsSubscriber(showTmdbId: Int) {
        isAddedOrAddingQueryListeners.remove(showTmdbId)?.destroy()
    }

    fun setIsArchivedSubscriber(showTmdbId: Int, subscriber: Subscriber<Boolean>) {
        removeIsArchivedSubscriber(showTmdbId)

        isArchivedQueryListeners[showTmdbId] = QueryListener(
            query = db.myShowQueries.isArchived(showTmdbId),
            subscriber = subscriber,
            notifyImmediately = true,
            extractQueryResult = Query<Boolean>::executeAsOne)
    }

    fun removeIsArchivedSubscriber(showTmdbId: Int) {
        isArchivedQueryListeners.remove(showTmdbId)?.destroy()
    }

    fun setLastWeekShowsSubscriber(subscriber: Subscriber<List<UpcomingShowViewModel>>) {
        removeLastWeekShowsSubscriber()

        val query = db.myShowQueries.lastWeekShows(::mapUpcomingShowViewModel)

        lastWeekShowsQueryListener = QueryListener(
            query = query,
            subscriber = subscriber,
            notifyImmediately = true,
            extractQueryResult = Query<UpcomingShowViewModel>::executeAsList)
    }

    fun removeLastWeekShowsSubscriber() {
        lastWeekShowsQueryListener?.destroy()
        lastWeekShowsQueryListener = null
    }

    fun triggerLastWeekShowsSubscriber() {
        lastWeekShowsQueryListener?.queryResultsChanged()
    }

    fun setUpcomingShowsSubscriber(subscriber: Subscriber<List<UpcomingShowViewModel>>) {
        removeUpcomingShowsSubscriber()

        val query = db.myShowQueries.upcomingShows(::mapUpcomingShowViewModel)

        upcomingShowsQueryListener = QueryListener(
            query = query,
            subscriber = subscriber,
            notifyImmediately = true,
            extractQueryResult = Query<UpcomingShowViewModel>::executeAsList)
    }

    fun removeUpcomingShowsSubscriber() {
        upcomingShowsQueryListener?.destroy()
        upcomingShowsQueryListener = null
    }

    fun triggerUpcomingShowsSubscriber() {
        upcomingShowsQueryListener?.queryResultsChanged()
    }

    fun setToBeAnnouncedShowsSubscriber(subscriber: Subscriber<List<ShowViewModel>>) {
        removeToBeAnnouncedShowsSubscriber()

        toBeAnnouncedShowsQueryListener = QueryListener(
            query = db.myShowQueries.toBeAnnouncedShows(mapper = ::mapShowViewModel),
            subscriber = subscriber,
            notifyImmediately = true,
            extractQueryResult = Query<ShowViewModel>::executeAsList)
    }

    fun removeToBeAnnouncedShowsSubscriber() {
        toBeAnnouncedShowsQueryListener?.destroy()
        toBeAnnouncedShowsQueryListener = null
    }

    fun setEndedShowsSubscriber(subscriber: Subscriber<List<ShowViewModel>>) {
        removeEndedShowsSubscriber()

        endedShowsQueryListener = QueryListener(
            query = db.myShowQueries.endedShows(mapper = ::mapShowViewModel),
            subscriber = subscriber,
            notifyImmediately = true,
            extractQueryResult = Query<ShowViewModel>::executeAsList)
    }

    fun removeEndedShowsSubscriber() {
        endedShowsQueryListener?.destroy()
        endedShowsQueryListener = null
    }

    fun setArchivedShowsSubscriber(subscriber: Subscriber<List<ShowViewModel>>) {
        removeArchivedShowsSubscriber()

        archivedShowsQueueListener = QueryListener(
            query = db.myShowQueries.archivedShows(mapper = ::mapArchivedShowViewModel),
            subscriber = subscriber,
            notifyImmediately = true,
            extractQueryResult = Query<ShowViewModel>::executeAsList)
    }

    fun removeArchivedShowsSubscriber() {
        archivedShowsQueueListener?.destroy()
        archivedShowsQueueListener = null
    }

    fun showDetails(tmdbId: Int): ShowDetails? {
        return db.myShowQueries.showDetails(tmdbId).executeAsOneOrNull()
    }

    fun archiveShow(showTmdbId: Int) {
        db.myShowQueries.archive(showTmdbId)
    }

    fun unarchiveShow(showTmdbId: Int) {
        db.myShowQueries.unarchive(showTmdbId)
    }

    companion object {

        fun mapShowViewModel(tmdbId: Int, name: String, imageUrl: String?): ShowViewModel {
            return mapShowViewModel(tmdbId, name, imageUrl, isArchived = false)
        }

        fun mapArchivedShowViewModel(tmdbId: Int, name: String, imageUrl: String?): ShowViewModel {
            return mapShowViewModel(tmdbId, name, imageUrl, isArchived = true)
        }

        private fun mapShowViewModel(tmdbId: Int, name: String, imageUrl: String?, isArchived: Boolean): ShowViewModel {
            return ShowViewModel(
                id = tmdbId,
                name = name,
                backdropUrl = imageUrl,
                isArchived = isArchived)
        }

        fun mapUpcomingShowViewModel(
            tmdbId: Int,
            name: String,
            episodeName: String,
            episodeNumber: Int,
            seasonNumber: Int,
            showImageUrl: String?,
            airDateMillis: Long?,
            episodeImageUrl: String?): UpcomingShowViewModel
        {
            val daysLeft: String = if (airDateMillis != null) {
                formatTimeBetween(now, GMTDate(airDateMillis))
            } else {
                "N/A"
            }

            return UpcomingShowViewModel(
                id = tmdbId,
                name = name,
                backdropUrl = episodeImageUrl ?: showImageUrl,
                episodeName = episodeName,
                episodeNumber = formatEpisodeNumber(season = seasonNumber, episode = episodeNumber),
                timeLeft = daysLeft)
        }
    }
}
