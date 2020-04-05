package dev.polek.episodetracker.common.presentation.myshows

import dev.polek.episodetracker.common.datasource.db.QueryListener.Subscriber
import dev.polek.episodetracker.common.presentation.BasePresenter
import dev.polek.episodetracker.common.presentation.myshows.model.MyShowsListItem.ShowViewModel
import dev.polek.episodetracker.common.presentation.myshows.model.MyShowsListItem.UpcomingShowViewModel
import dev.polek.episodetracker.common.presentation.myshows.model.MyShowsViewModel
import dev.polek.episodetracker.common.repositories.MyShowsRepository

class MyShowsPresenter(private val repository: MyShowsRepository) : BasePresenter<MyShowsView>() {

    private var model = MyShowsViewModel(
        upcomingShows = emptyList(),
        toBeAnnouncedShows = emptyList(),
        endedShows = emptyList(),
        archivedShows = emptyList())

    private val upcomingShowsSubscriber = object : Subscriber<List<UpcomingShowViewModel>> {
        override fun onQueryResult(result: List<UpcomingShowViewModel>) {
            model = model.modified(upcomingShows = result)
            view?.displayUpcomingShows(model)
        }
    }

    private val toBeAnnouncedShowsSubscriber = object : Subscriber<List<ShowViewModel>> {
        override fun onQueryResult(result: List<ShowViewModel>) {
            model = model.modified(toBeAnnouncedShows = result)
            view?.displayToBeAnnouncedShows(model)
        }
    }

    private val endedShowsSubscriber = object : Subscriber<List<ShowViewModel>> {
        override fun onQueryResult(result: List<ShowViewModel>) {
            model = model.modified(endedShows = result)
            view?.displayEndedShows(model)
        }
    }

    private val archivedShowsSubscriber = object : Subscriber<List<ShowViewModel>> {
        override fun onQueryResult(result: List<ShowViewModel>) {
            model = model.modified(archivedShows = result)
            view?.displayArchivedShows(model)
        }
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

    fun onRemoveShowClicked(show: ShowViewModel) {
        repository.removeShow(show.id)
    }

    fun onArchiveShowClicked(show: ShowViewModel) {
        repository.archiveShow(show.id)
    }

    fun onUnarchiveShowClicked(show: ShowViewModel) {
        repository.unarchiveShow(show.id)
    }
}
