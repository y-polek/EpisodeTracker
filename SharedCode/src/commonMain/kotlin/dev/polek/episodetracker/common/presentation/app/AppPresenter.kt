package dev.polek.episodetracker.common.presentation.app

import dev.polek.episodetracker.common.presentation.BasePresenter
import dev.polek.episodetracker.common.repositories.MyShowsRepository

class AppPresenter(
    private val myShowsRepository: MyShowsRepository) : BasePresenter<AppView>()
{
    override fun onViewAppeared() {
        super.onViewAppeared()
        refreshUpcomingShows()
    }

    private fun refreshUpcomingShows() {
        myShowsRepository.triggerLastWeekShowsSubscriber()
        myShowsRepository.triggerUpcomingShowsSubscriber()
    }
}
