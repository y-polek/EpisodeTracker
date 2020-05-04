package dev.polek.episodetracker.common.presentation.myshows

import com.russhwolf.settings.ExperimentalListener
import com.russhwolf.settings.SettingsListener
import dev.polek.episodetracker.common.datasource.db.QueryListener.Subscriber
import dev.polek.episodetracker.common.preferences.Preferences
import dev.polek.episodetracker.common.presentation.BasePresenter
import dev.polek.episodetracker.common.presentation.myshows.model.MyShowsListItem.ShowViewModel
import dev.polek.episodetracker.common.presentation.myshows.model.MyShowsListItem.UpcomingShowViewModel
import dev.polek.episodetracker.common.repositories.MyShowsRepository
import dev.polek.episodetracker.common.repositories.ShowRepository
import kotlinx.coroutines.launch

class MyShowsPresenter(
    private val myShowsRepository: MyShowsRepository,
    private val showRepository: ShowRepository,
    private val prefs: Preferences) : BasePresenter<MyShowsView>()
{
    private var lastWeekShows = emptyList<UpcomingShowViewModel>()
    private var filteredLastWeekShows = emptyList<UpcomingShowViewModel>()

    private var upcomingShows = emptyList<UpcomingShowViewModel>()
    private var filteredUpcomingShows = emptyList<UpcomingShowViewModel>()

    private var tbaShows = emptyList<ShowViewModel>()
    private var filteredTbaShows = emptyList<ShowViewModel>()

    private var endedShows = emptyList<ShowViewModel>()
    private var filteredEndedShows = emptyList<ShowViewModel>()

    private var archivedShows = emptyList<ShowViewModel>()
    private var filteredArchivedShows = emptyList<ShowViewModel>()

    private var searchQuery = ""

    private val isFiltered: Boolean
        get() = searchQuery.isNotEmpty() && ((lastWeekShows.isNotEmpty() && prefs.showLastWeekSection) || upcomingShows.isNotEmpty() || tbaShows.isNotEmpty() || endedShows.isNotEmpty() || archivedShows.isNotEmpty())

    private val lastWeekShowsSubscriber = object : Subscriber<List<UpcomingShowViewModel>> {
        override fun onQueryResult(result: List<UpcomingShowViewModel>) {
            lastWeekShows = result
            filteredLastWeekShows = result.filtered()
            view?.displayLastWeekShows(filteredLastWeekShows, isVisible = prefs.showLastWeekSection)
            showOrHideEmptyMessage()
        }
    }

    private val upcomingShowsSubscriber = object : Subscriber<List<UpcomingShowViewModel>> {
        override fun onQueryResult(result: List<UpcomingShowViewModel>) {
            upcomingShows = result
            filteredUpcomingShows = result.filtered()
            view?.displayUpcomingShows(filteredUpcomingShows)
            showOrHideEmptyMessage()
        }
    }

    private val toBeAnnouncedShowsSubscriber = object : Subscriber<List<ShowViewModel>> {
        override fun onQueryResult(result: List<ShowViewModel>) {
            tbaShows = result
            filteredTbaShows = result.filtered()
            view?.displayToBeAnnouncedShows(filteredTbaShows)
            showOrHideEmptyMessage()
        }
    }

    private val endedShowsSubscriber = object : Subscriber<List<ShowViewModel>> {
        override fun onQueryResult(result: List<ShowViewModel>) {
            endedShows = result
            filteredEndedShows = result.filtered()
            view?.displayEndedShows(filteredEndedShows)
            showOrHideEmptyMessage()
        }
    }

    private val archivedShowsSubscriber = object : Subscriber<List<ShowViewModel>> {
        override fun onQueryResult(result: List<ShowViewModel>) {
            archivedShows = result
            filteredArchivedShows = result.filtered()
            view?.displayArchivedShows(filteredArchivedShows)
            showOrHideEmptyMessage()
        }
    }

    @OptIn(ExperimentalListener::class)
    private var showLastWeekSectionListener: SettingsListener? = null

    var isLastWeekExpanded: Boolean
        get() = prefs.isLastWeekExpanded
        set(value) {
            prefs.isLastWeekExpanded = value
        }
    var isUpcomingExpanded: Boolean
        get() = prefs.isUpcomingExpanded
        set(value) {
            prefs.isUpcomingExpanded = value
        }
    var isTbaExpanded: Boolean
        get() = prefs.isTbaExpanded
        set(value) {
            prefs.isTbaExpanded = value
        }
    var isEndedExpanded: Boolean
        get() = prefs.isEndedExpanded
        set(value) {
            prefs.isEndedExpanded = value
        }
    var isArchivedExpanded: Boolean
        get() = prefs.isArchivedExpanded
        set(value) {
            prefs.isArchivedExpanded = value
        }

    override fun onViewAppeared() {
        super.onViewAppeared()
        myShowsRepository.setLastWeekShowsSubscriber(lastWeekShowsSubscriber)
        myShowsRepository.setUpcomingShowsSubscriber(upcomingShowsSubscriber)
        myShowsRepository.setToBeAnnouncedShowsSubscriber(toBeAnnouncedShowsSubscriber)
        myShowsRepository.setEndedShowsSubscriber(endedShowsSubscriber)
        myShowsRepository.setArchivedShowsSubscriber(archivedShowsSubscriber)

        showLastWeekSectionListener = prefs.listenShowLastWeekSectionListener {
            myShowsRepository.triggerLastWeekShowsSubscriber()
        }
    }

    @OptIn(ExperimentalListener::class)
    override fun onViewDisappeared() {
        myShowsRepository.removeLastWeekShowsSubscriber()
        myShowsRepository.removeUpcomingShowsSubscriber()
        myShowsRepository.removeToBeAnnouncedShowsSubscriber()
        myShowsRepository.removeEndedShowsSubscriber()
        myShowsRepository.removeArchivedShowsSubscriber()

        showLastWeekSectionListener?.deactivate()

        super.onViewDisappeared()
    }

    fun onShowClicked(show: ShowViewModel) {
        view?.openMyShowDetails(show)
    }

    fun onRemoveShowClicked(show: ShowViewModel) {
        myShowsRepository.removeShow(show.id)
    }

    fun onArchiveShowClicked(show: ShowViewModel) {
        myShowsRepository.archiveShow(show.id)
    }

    fun onUnarchiveShowClicked(show: ShowViewModel) {
        myShowsRepository.unarchiveShow(show.id)
    }

    fun onSearchQueryChanged(text: String) {
        searchQuery = text.trim()

        filteredLastWeekShows = lastWeekShows.filtered()
        view?.displayLastWeekShows(filteredLastWeekShows, isVisible = prefs.showLastWeekSection)

        filteredUpcomingShows = upcomingShows.filtered()
        view?.displayUpcomingShows(filteredUpcomingShows)

        filteredTbaShows = tbaShows.filtered()
        view?.displayToBeAnnouncedShows(filteredTbaShows)

        filteredEndedShows = endedShows.filtered()
        view?.displayEndedShows(filteredEndedShows)

        filteredArchivedShows = archivedShows.filtered()
        view?.displayArchivedShows(filteredArchivedShows)

        showOrHideEmptyMessage()
    }

    fun onRefreshRequested() {
        launch {
            showRepository.refreshAllNonArchivedShows()
            view?.hideRefresh()
        }
    }

    private fun showOrHideEmptyMessage() {
        if (filteredLastWeekShows.isEmpty()
            && filteredUpcomingShows.isEmpty()
            && filteredTbaShows.isEmpty()
            && filteredEndedShows.isEmpty()
            && filteredArchivedShows.isEmpty())
        {
            view?.showEmptyMessage(isFiltered)
        } else {
            view?.hideEmptyMessage()
        }
    }

    private fun <T: ShowViewModel> List<T>.filtered(): List<T> {
        if (searchQuery.isEmpty()) return this
        return this.filter { show ->
            show.name.contains(searchQuery, ignoreCase = true)
        }
    }
}
