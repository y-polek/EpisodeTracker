package dev.polek.episodetracker.common.presentation.app

import dev.polek.episodetracker.common.preferences.Preferences
import dev.polek.episodetracker.common.presentation.BasePresenter
import dev.polek.episodetracker.common.repositories.MyShowsRepository
import dev.polek.episodetracker.common.repositories.ShowRepository
import dev.polek.episodetracker.common.utils.now
import dev.polek.episodetracker.common.utils.isSameDayAs
import io.ktor.util.date.GMTDate
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AppPresenter(
    private val preferences: Preferences,
    private val myShowsRepository: MyShowsRepository,
    private val showRepository: ShowRepository) : BasePresenter<AppView>()
{
    override fun onViewAppeared() {
        super.onViewAppeared()
        refreshAllShowsIfRequired()
        refreshUpcomingShows()
    }

    private fun refreshAllShowsIfRequired() {
        val lastRefreshDate = GMTDate(preferences.lastRefreshTimestamp)
        if (!lastRefreshDate.isSameDayAs(now)) {
            launch {
                showRepository.refreshAllNonArchivedShows()
            }
        }
    }

    private fun refreshUpcomingShows() {
        myShowsRepository.triggerLastWeekShowsSubscriber()
        myShowsRepository.triggerUpcomingShowsSubscriber()
    }
}
