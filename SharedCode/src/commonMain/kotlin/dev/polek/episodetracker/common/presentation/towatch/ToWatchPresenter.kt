package dev.polek.episodetracker.common.presentation.towatch

import dev.polek.episodetracker.common.presentation.BasePresenter
import dev.polek.episodetracker.common.repositories.ToWatchRepository

class ToWatchPresenter(private val repository: ToWatchRepository) : BasePresenter<ToWatchView>() {

    override fun attachView(view: ToWatchView) {
        super.attachView(view)

        loadShows()
    }

    fun onWatchedButtonClicked(show: ToWatchShowViewModel) {
        repository.markEpisodeWatched(show.episodeId)
        val updatedShow = repository.toWatchShow(showId = show.id)
        if (updatedShow != null) {
            view?.updateShow(updatedShow)
        } else {
            view?.removeShow(show)
        }
    }

    private fun loadShows() {
        val shows = repository.toWatchShows()
        view?.displayShows(shows)
    }
}
