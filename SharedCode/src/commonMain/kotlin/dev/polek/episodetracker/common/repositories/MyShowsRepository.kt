package dev.polek.episodetracker.common.repositories

import com.squareup.sqldelight.Query
import dev.polek.episodetracker.common.datasource.db.QueryListener
import dev.polek.episodetracker.common.datasource.db.QueryListener.Subscriber
import dev.polek.episodetracker.common.logging.logw
import dev.polek.episodetracker.common.presentation.myshows.model.MyShowsListItem.ShowViewModel
import dev.polek.episodetracker.common.presentation.myshows.model.MyShowsListItem.UpcomingShowViewModel
import dev.polek.episodetracker.common.utils.formatEpisodeNumber
import dev.polek.episodetracker.common.utils.formatTimeBetween
import dev.polek.episodetracker.db.Database
import dev.polek.episodetracker.db.ShowDetails
import io.ktor.util.date.GMTDate

class MyShowsRepository(
    private val db: Database,
    private val addToMyShowsQueue: AddToMyShowsQueue)
{
    private var upcomingShowsQueryListener: QueryListener<UpcomingShowViewModel, List<UpcomingShowViewModel>>? = null
    private var toBeAnnouncedShowsQueryListener: QueryListener<ShowViewModel, List<ShowViewModel>>? = null
    private var endedShowsQueryListener: QueryListener<ShowViewModel, List<ShowViewModel>>? = null
    private var isAddedOrAddingQueryListeners = mutableMapOf<Int, QueryListener<Boolean, Boolean>>()

    fun addShow(tmdbId: Int) {
        if (isAddedOrAddingToMyShows(tmdbId)) {
            logw { "Trying to add Show that's already in My Shows" }
            return
        }

        addToMyShowsQueue.addShow(tmdbId)
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
        val isAdded = isAddedToMyShows(tmdbId)
        val isAdding = db.addToMyShowsTaskQueries.allTasks().executeAsList().any { it.showTmdbId == tmdbId }
        return isAdded || isAdding
    }

    fun setIsAddedOrAddingToMyShowsSubscriber(showTmdbId: Int, subscriber: Subscriber<Boolean>) {
        removeIsAddedOrAddingToMyShowsSubscriber(showTmdbId)

        val query = db.myShowQueries.isAddedOrAdding(showTmdbId)
        isAddedOrAddingQueryListeners[showTmdbId] = QueryListener(
            query = query,
            subscriber = subscriber,
            notifyImmediately = true,
            extractQueryResult = Query<Boolean>::executeAsOne)
    }

    fun removeIsAddedOrAddingToMyShowsSubscriber(showTmdbId: Int) {
        isAddedOrAddingQueryListeners.remove(showTmdbId)?.destroy()
    }

    fun setUpcomingShowsSubscriber(subscriber: Subscriber<List<UpcomingShowViewModel>>) {
        removeUpcomingShowsSubscriber()

        val now = GMTDate()
        val query = db.myShowQueries.upcomingShows { tmdbId, name, episodeName, episodeNumber, seasonNumber, airDateMillis, imageUrl ->
            val daysLeft: String = if (airDateMillis != null) {
                formatTimeBetween(now, GMTDate(airDateMillis))
            } else {
                "N/A"
            }

            val show = UpcomingShowViewModel(
                id = tmdbId,
                name = name,
                backdropUrl = imageUrl,
                episodeName = episodeName,
                episodeNumber = formatEpisodeNumber(season = seasonNumber, episode = episodeNumber),
                timeLeft = daysLeft)
            show
        }

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

    fun showDetails(tmdbId: Int): ShowDetails? {
        return db.myShowQueries.showDetails(tmdbId).executeAsOneOrNull()
    }

    companion object {
        fun mapShowViewModel(tmdbId: Int, name: String, imageUrl: String?): ShowViewModel {
            return ShowViewModel(
                id = tmdbId,
                name = name,
                backdropUrl = imageUrl)
        }
    }
}
