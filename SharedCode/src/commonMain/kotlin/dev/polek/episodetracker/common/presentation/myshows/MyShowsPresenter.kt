package dev.polek.episodetracker.common.presentation.myshows

import dev.polek.episodetracker.common.presentation.BasePresenter
import dev.polek.episodetracker.common.repositories.MyShowsRepository
import dev.polek.episodetracker.common.presentation.myshows.model.MyShowsViewModel

class MyShowsPresenter(private val repository: MyShowsRepository) : BasePresenter<MyShowsView>() {

    override fun attachView(view: MyShowsView) {
        super.attachView(view)

        val model =
            MyShowsViewModel(
                upcomingShows = repository.upcomingShows(),
                toBeAnnouncedShows = repository.toBeAnnouncedShows(),
                endedShows = repository.endedShows(),
                isUpcomingExpanded = true,
                isToBeAnnouncedExpanded = true,
                isEndedExpanded = true
            )

        view.updateShows(model)

        repository.printAllShows()
    }
}
