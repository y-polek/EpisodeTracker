package dev.polek.episodetracker.common.presentation.app

import dev.polek.episodetracker.common.analytics.Analytics
import dev.polek.episodetracker.common.datasource.db.QueryListener.Subscriber
import dev.polek.episodetracker.common.di.Inject
import dev.polek.episodetracker.common.di.Singleton
import dev.polek.episodetracker.common.preferences.Preferences
import dev.polek.episodetracker.common.presentation.BasePresenter
import dev.polek.episodetracker.common.repositories.MyShowsRepository
import dev.polek.episodetracker.common.repositories.ShowRepository
import dev.polek.episodetracker.common.utils.isSameDayAs
import dev.polek.episodetracker.common.utils.now
import io.ktor.util.date.GMTDate
import kotlinx.coroutines.launch

@Singleton
class AppPresenter @Inject constructor(
    private val preferences: Preferences,
    private val myShowsRepository: MyShowsRepository,
    private val showRepository: ShowRepository,
    private val analytics: Analytics) : BasePresenter<AppView>()
{
    private val numberOfShowsSubscriber = object : Subscriber<Long> {
        private var lastResult: Long = -1

        override fun onQueryResult(result: Long) {
            if (result == lastResult) return
            lastResult = result
            analytics.logNumberOfShows(result.toInt())
        }
    }

    override fun attachView(view: AppView) {
        super.attachView(view)
        view.setAppearance(preferences.appearance)
    }

    override fun onViewAppeared() {
        super.onViewAppeared()
        refreshAllShowsIfRequired()
        refreshUpcomingShows()
        myShowsRepository.setNumberOfShowsSubscriber(numberOfShowsSubscriber)
    }

    override fun onViewDisappeared() {
        myShowsRepository.removeNumberOfShowsSubscriber()
        super.onViewDisappeared()
    }

    private fun refreshAllShowsIfRequired() {
        val lastRefreshDate = GMTDate(preferences.lastRefreshTimestamp)
        if (!lastRefreshDate.isSameDayAs(now)) {
            launch {
                showRepository.refreshAllShows()
            }
        }
    }

    private fun refreshUpcomingShows() {
        myShowsRepository.triggerLastWeekShowsSubscriber()
        myShowsRepository.triggerUpcomingShowsSubscriber()
    }
}
