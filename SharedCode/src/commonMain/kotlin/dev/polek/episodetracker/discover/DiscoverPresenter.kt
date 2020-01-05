package dev.polek.episodetracker.discover

import dev.polek.episodetracker.common.presentation.BasePresenter
import dev.polek.episodetracker.common.repositories.DiscoverRepository
import dev.polek.episodetracker.common.repositories.MyShowsRepository
import dev.polek.episodetracker.discover.model.DiscoverResultViewModel
import kotlinx.coroutines.launch

class DiscoverPresenter(
    private val discoverRepository: DiscoverRepository,
    private val myShowsRepository: MyShowsRepository) : BasePresenter<DiscoverView>()
{
    override fun attachView(view: DiscoverView) {
        super.attachView(view)

        view.showPrompt()
    }

    fun onSearchQuerySubmitted(query: String) {
        view?.hidePrompt()
        view?.hideEmptyMessage()
        view?.showProgress()

        launch {
            val results = discoverRepository.search(query).map {
                DiscoverResultViewModel(
                    tmdbId = it.id,
                    name = it.name,
                    year = it.year,
                    posterUrl = it.posterUrl,
                    overview = it.overview,
                    genres = it.genres,
                    isInMyShows = false
                )
            }

            view?.hideProgress()
            view?.showSearchResults(results)
            if (results.isEmpty()) {
                view?.showEmptyMessage()
            }
        }
    }

    fun onAddButtonClicked(show: DiscoverResultViewModel) {
        view?.showProgress()

        launch {
            myShowsRepository.addShow(show.tmdbId)

            view?.hideProgress()
        }
    }
}
