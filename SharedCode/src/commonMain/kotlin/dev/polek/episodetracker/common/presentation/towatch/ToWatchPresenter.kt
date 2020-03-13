package dev.polek.episodetracker.common.presentation.towatch

import co.touchlab.stately.ensureNeverFrozen
import dev.polek.episodetracker.common.datasource.db.QueryListener
import dev.polek.episodetracker.common.model.ToWatchShow
import dev.polek.episodetracker.common.presentation.BasePresenter
import dev.polek.episodetracker.common.repositories.ToWatchRepository

class ToWatchPresenter(private val repository: ToWatchRepository) : BasePresenter<ToWatchView>(),
    QueryListener.Subscriber<List<ToWatchShow>> {

    init {
        ensureNeverFrozen()
    }

    override fun onViewAppeared() {
        repository.setToWatchShowsSubscriber(this)
    }

    override fun onViewDisappeared() {
        repository.removeToWatchShowsSubscriber()
        super.onViewDisappeared()
    }

    fun onWatchedButtonClicked(show: ToWatchShowViewModel) {
        repository.markNextEpisodeWatched(showTmdbId = show.id)
    }

    fun onShowClicked(show: ToWatchShowViewModel) {
        view?.openToWatchShowDetails(show)
    }

    override fun onQueryResult(result: List<ToWatchShow>) {
        val shows = result.map(ToWatchShowViewModel.Companion::map)
        view?.displayShows(shows)
    }
}
