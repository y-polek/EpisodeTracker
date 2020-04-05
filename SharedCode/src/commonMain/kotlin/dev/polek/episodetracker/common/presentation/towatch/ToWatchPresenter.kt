package dev.polek.episodetracker.common.presentation.towatch

import co.touchlab.stately.ensureNeverFrozen
import dev.polek.episodetracker.common.datasource.db.QueryListener
import dev.polek.episodetracker.common.model.ToWatchShow
import dev.polek.episodetracker.common.presentation.BasePresenter
import dev.polek.episodetracker.common.repositories.EpisodesRepository
import dev.polek.episodetracker.common.repositories.ToWatchRepository

class ToWatchPresenter(
    private val toWatchRepository: ToWatchRepository,
    private val episodesRepository: EpisodesRepository) : BasePresenter<ToWatchView>(), QueryListener.Subscriber<List<ToWatchShow>>
{
    init {
        ensureNeverFrozen()
    }

    override fun onViewAppeared() {
        toWatchRepository.setToWatchShowsSubscriber(this)
    }

    override fun onViewDisappeared() {
        toWatchRepository.removeToWatchShowsSubscriber()
        super.onViewDisappeared()
    }

    fun onWatchedButtonClicked(show: ToWatchShowViewModel) {
        episodesRepository.markNextEpisodeWatched(showTmdbId = show.id)
    }

    fun onShowClicked(show: ToWatchShowViewModel) {
        view?.openToWatchShowDetails(show)
    }

    fun onMarkAllWatchedClicked(show: ToWatchShowViewModel) {
        episodesRepository.markAllWatched(show.id)
    }

    override fun onQueryResult(result: List<ToWatchShow>) {
        val shows = result.map(ToWatchShowViewModel.Companion::map)
        view?.displayShows(shows)
    }
}
