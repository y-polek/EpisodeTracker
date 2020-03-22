package dev.polek.episodetracker.common.presentation.myshows

import dev.polek.episodetracker.common.datasource.db.QueryListener.Subscriber
import dev.polek.episodetracker.common.presentation.BasePresenter
import dev.polek.episodetracker.common.presentation.myshows.model.MyShowsListItem.ShowViewModel
import dev.polek.episodetracker.common.presentation.myshows.model.MyShowsListItem.UpcomingShowViewModel
import dev.polek.episodetracker.common.presentation.myshows.model.MyShowsViewModel
import dev.polek.episodetracker.common.repositories.MyShowsRepository

class MyShowsPresenter(private val repository: MyShowsRepository) : BasePresenter<MyShowsView>() {

    private val model = MyShowsViewModel(
        upcomingShows = emptyList(),
        toBeAnnouncedShows = emptyList(),
        endedShows = emptyList(),
        archivedShows = emptyList(),
        isUpcomingExpanded = true,
        isToBeAnnouncedExpanded = true,
        isEndedExpanded = true,
        isArchivedExpanded = true)

    private val upcomingShowsSubscriber = object : Subscriber<List<UpcomingShowViewModel>> {
        override fun onQueryResult(result: List<UpcomingShowViewModel>) {
            model.upcomingShows = result
            view?.updateShows(model)
        }
    }

    private val toBeAnnouncedShowsSubscriber = object : Subscriber<List<ShowViewModel>> {
        override fun onQueryResult(result: List<ShowViewModel>) {
            model.toBeAnnouncedShows = result
            view?.updateShows(model)
        }
    }

    private val endedShowsSubscriber = object : Subscriber<List<ShowViewModel>> {
        override fun onQueryResult(result: List<ShowViewModel>) {
            model.endedShows = result
            view?.updateShows(model)
        }
    }

    private val archivedShowsSubscriber = object : Subscriber<List<ShowViewModel>> {
        override fun onQueryResult(result: List<ShowViewModel>) {
            model.archivedShows = result
            view?.updateShows(model)
        }
    }

    override fun attachView(view: MyShowsView) {
        super.attachView(view)
        view.updateShows(model)
    }

    override fun onViewAppeared() {
        super.onViewAppeared()
        repository.setUpcomingShowsSubscriber(upcomingShowsSubscriber)
        repository.setToBeAnnouncedShowsSubscriber(toBeAnnouncedShowsSubscriber)
        repository.setEndedShowsSubscriber(endedShowsSubscriber)
        repository.setArchivedShowsSubscriber(archivedShowsSubscriber)
    }

    override fun onViewDisappeared() {
        repository.removeUpcomingShowsSubscriber()
        repository.removeToBeAnnouncedShowsSubscriber()
        repository.removeEndedShowsSubscriber()
        repository.removeArchivedShowsSubscriber()
        super.onViewDisappeared()
    }

    fun onShowClicked(show: ShowViewModel) {
        view?.openMyShowDetails(show)
    }
}
