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
                    tmdbId = it.tmdbId,
                    name = it.name,
                    year = it.year,
                    posterUrl = it.posterUrl,
                    overview = it.overview,
                    genres = it.genres,
                    isInMyShows = myShowsRepository.isInMyShows(it.tmdbId)
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
        show.isAddInProgress = true
        view?.updateSearchResult(show)

        launch {
            myShowsRepository.addShow(show.tmdbId)
            show.isInMyShows = true
            show.isAddInProgress = false
            view?.updateSearchResult(show)
            view?.hideProgress()
        }
    }
}
