package dev.polek.episodetracker.common.presentation.towatch

import co.touchlab.stately.ensureNeverFrozen
import dev.polek.episodetracker.common.datasource.db.QueryListener.Subscriber
import dev.polek.episodetracker.common.model.ToWatchShow
import dev.polek.episodetracker.common.presentation.BasePresenter
import dev.polek.episodetracker.common.repositories.EpisodesRepository
import dev.polek.episodetracker.common.repositories.MyShowsRepository
import dev.polek.episodetracker.common.repositories.ToWatchRepository

class ToWatchPresenter(
    private val toWatchRepository: ToWatchRepository,
    private val episodesRepository: EpisodesRepository,
    private val myShowsRepository: MyShowsRepository) : BasePresenter<ToWatchView>()
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
    }

    fun onSearchQueryChanged(text: String) {
        searchQuery = text.trim()
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
