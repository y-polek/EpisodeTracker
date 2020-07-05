package dev.polek.episodetracker.common.presentation.towatch

import co.touchlab.stately.ensureNeverFrozen
import dev.polek.episodetracker.common.analytics.Analytics
import dev.polek.episodetracker.common.analytics.Analytics.Screen
import dev.polek.episodetracker.common.datasource.db.QueryListener.Subscriber
import dev.polek.episodetracker.common.di.Inject
import dev.polek.episodetracker.common.di.Singleton
import dev.polek.episodetracker.common.model.ToWatchShow
import dev.polek.episodetracker.common.presentation.BasePresenter
import dev.polek.episodetracker.common.repositories.EpisodesRepository
import dev.polek.episodetracker.common.repositories.MyShowsRepository
import dev.polek.episodetracker.common.repositories.ToWatchRepository

@Singleton
class ToWatchPresenter @Inject constructor(
    private val toWatchRepository: ToWatchRepository,
    private val episodesRepository: EpisodesRepository,
    private val myShowsRepository: MyShowsRepository,
    private val analytics: Analytics) : BasePresenter<ToWatchView>()
{
    private var shows = emptyList<ToWatchShowViewModel>()
    private var searchQuery = ""
    private val isFiltered: Boolean
        get() = searchQuery.isNotEmpty() && shows.isNotEmpty()

    private val toWatchShowsSubscriber = object : Subscriber<List<ToWatchShow>> {
        override fun onQueryResult(result: List<ToWatchShow>) {
            shows = result.map(ToWatchShowViewModel.Companion::map).sortedBy(ToWatchShowViewModel::name)
            displayShows()
        }
    }

    init {
        ensureNeverFrozen()
    }

    override fun onViewAppeared() {
        toWatchRepository.setToWatchShowsSubscriber(toWatchShowsSubscriber)
    }

    override fun onViewDisappeared() {
        toWatchRepository.removeToWatchShowsSubscriber()
        super.onViewDisappeared()
    }

    fun onWatchedButtonClicked(show: ToWatchShowViewModel) {
        if (show.isSpecials) {
            episodesRepository.markNextSpecialEpisodeWatched(showTmdbId = show.id)
        } else {
            episodesRepository.markNextEpisodeWatched(showTmdbId = show.id)
        }
    }

    fun onShowClicked(show: ToWatchShowViewModel) {
        view?.openToWatchShowDetails(show, show.nextEpisodeNumber)
        analytics.logOpenDetails(show.id, Screen.TO_WATCH)
    }

    fun onMarkAllWatchedClicked(show: ToWatchShowViewModel) {
        if (show.isSpecials) {
            episodesRepository.markAllSpecialsWatched(show.id)
        } else {
            episodesRepository.markAllWatched(show.id)
        }
    }

    fun onArchiveShowClicked(show: ToWatchShowViewModel) {
        myShowsRepository.archiveShow(show.id)
        analytics.logArchiveShow(show.id, Screen.TO_WATCH)
    }

    fun onSearchQueryChanged(text: String) {
        val query = text.trim()
        if (query == searchQuery) return

        searchQuery = query
        displayShows()
    }

    private fun displayShows() {
        val filteredShows = shows.filter { show ->
            show.name.contains(searchQuery, ignoreCase = true)
        }
        view?.displayShows(filteredShows)
        if (filteredShows.isEmpty()) {
            view?.showEmptyMessage(isFiltered)
        } else {
            view?.hideEmptyMessage()
        }
    }
}
