package dev.polek.episodetracker.common.presentation.towatch

import dev.polek.episodetracker.common.presentation.BasePresenter
import dev.polek.episodetracker.common.repositories.ToWatchRepository

class ToWatchPresenter(private val repository: ToWatchRepository) : BasePresenter<ToWatchView>() {

    override fun attachView(view: ToWatchView) {
        super.attachView(view)

        loadShows()
    }

    private fun loadShows() {
        val shows = repository.toWatchShows()
        view?.displayShows(shows)
    }
}
