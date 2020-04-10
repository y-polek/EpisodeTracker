package dev.polek.episodetracker.common.presentation.myshows

import dev.polek.episodetracker.common.datasource.db.QueryListener.Subscriber
import dev.polek.episodetracker.common.presentation.BasePresenter
import dev.polek.episodetracker.common.presentation.myshows.model.MyShowsListItem.ShowViewModel
import dev.polek.episodetracker.common.presentation.myshows.model.MyShowsListItem.UpcomingShowViewModel
import dev.polek.episodetracker.common.repositories.MyShowsRepository

class MyShowsPresenter(private val repository: MyShowsRepository) : BasePresenter<MyShowsView>() {

    private var upcomingShows = emptyList<UpcomingShowViewModel>()
    private var tbaShows = emptyList<ShowViewModel>()
    private var endedShows = emptyList<ShowViewModel>()
    private var archivedShows = emptyList<ShowViewModel>()
    private var searchQuery = ""
    val isFiltering: Boolean
        get() = searchQuery.isNotEmpty() && (upcomingShows.isNotEmpty() || tbaShows.isNotEmpty() || endedShows.isNotEmpty() || archivedShows.isNotEmpty())

    private val upcomingShowsSubscriber = object : Subscriber<List<UpcomingShowViewModel>> {
        override fun onQueryResult(result: List<UpcomingShowViewModel>) {
            upcomingShows = result
            view?.displayUpcomingShows(result.filtered())
        }
    }

    private val toBeAnnouncedShowsSubscriber = object : Subscriber<List<ShowViewModel>> {
        override fun onQueryResult(result: List<ShowViewModel>) {
            tbaShows = result
            view?.displayToBeAnnouncedShows(result.filtered())
        }
    }

    private val endedShowsSubscriber = object : Subscriber<List<ShowViewModel>> {
        override fun onQueryResult(result: List<ShowViewModel>) {
            endedShows = result
            view?.displayEndedShows(result.filtered())
        }
    }

    private val archivedShowsSubscriber = object : Subscriber<List<ShowViewModel>> {
        override fun onQueryResult(result: List<ShowViewModel>) {
            archivedShows = result
            view?.displayArchivedShows(result.filtered())
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

    fun onSearchQueryChanged(text: String) {
        searchQuery = text.trim()

        view?.displayUpcomingShows(upcomingShows.filtered())
        view?.displayToBeAnnouncedShows(tbaShows.filtered())
        view?.displayEndedShows(endedShows.filtered())
        view?.displayArchivedShows(archivedShows.filtered())
    }

    private fun <T: ShowViewModel> List<T>.filtered(): List<T> {
        if (searchQuery.isEmpty()) return this
        return this.filter { show ->
            show.name.contains(searchQuery, ignoreCase = true)
        }
    }
}
