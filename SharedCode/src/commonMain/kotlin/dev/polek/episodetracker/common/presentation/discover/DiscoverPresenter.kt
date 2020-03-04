package dev.polek.episodetracker.common.presentation.discover

import dev.polek.episodetracker.common.presentation.BasePresenter
import dev.polek.episodetracker.common.presentation.discover.model.DiscoverResultViewModel
import dev.polek.episodetracker.common.repositories.DiscoverRepository
import dev.polek.episodetracker.common.repositories.MyShowsRepository
import kotlinx.coroutines.launch

class DiscoverPresenter(
    private val discoverRepository: DiscoverRepository,
    private val myShowsRepository: MyShowsRepository) : BasePresenter<DiscoverView>()
{
    private var searchResults: List<DiscoverResultViewModel>? = null

    override fun attachView(view: DiscoverView) {
        super.attachView(view)
        view.showPrompt()
    }

    override fun onViewAppeared() {
        super.onViewAppeared()

        searchResults?.forEach { show ->
            show.isInMyShows = myShowsRepository.isInMyShows(show.id)
        }
        view?.updateSearchResults()
    }

    fun onSearchQuerySubmitted(query: String) {
        view?.hidePrompt()
        view?.hideEmptyMessage()
        view?.showProgress()

        launch {
            val results = discoverRepository.search(query).map {
                DiscoverResultViewModel(
                    id = it.tmdbId,
                    name = it.name,
                    year = it.year,
                    posterUrl = it.posterUrl,
                    overview = it.overview,
                    genres = it.genres,
                    isInMyShows = myShowsRepository.isInMyShows(it.tmdbId)
                )
            }
            searchResults = results

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
            myShowsRepository.addShow(show.id)
            show.isInMyShows = true
            show.isAddInProgress = false
            view?.updateSearchResult(show)
            view?.hideProgress()
        }
    }

    fun onRemoveButtonClicked(show: DiscoverResultViewModel) {
        show.isInMyShows = false
        show.isAddInProgress = false
        view?.updateSearchResult(show)

        launch {
            myShowsRepository.removeShow(show.id)
        }
    }

    fun onShowClicked(show: DiscoverResultViewModel) {
        view?.openDiscoverShow(show.id)
    }
}
