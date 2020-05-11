package dev.polek.episodetracker.common.presentation.discover

import dev.polek.episodetracker.common.presentation.BasePresenter
import dev.polek.episodetracker.common.presentation.discover.model.DiscoverResultViewModel
import dev.polek.episodetracker.common.repositories.DiscoverRepository
import dev.polek.episodetracker.common.repositories.MyShowsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DiscoverPresenter(
    private val discoverRepository: DiscoverRepository,
    private val myShowsRepository: MyShowsRepository) : BasePresenter<DiscoverView>()
{
    private var searchResults: List<DiscoverResultViewModel>? = null

    private var lastSearchQuery: String? = null

    override fun attachView(view: DiscoverView) {
        super.attachView(view)
        view.showPrompt()
    }

    override fun onViewAppeared() {
        super.onViewAppeared()

        searchResults?.forEach { show ->
            show.isInMyShows = myShowsRepository.isAddedOrAddingToMyShows(show.id)
        }
        view?.updateSearchResults()
    }

    fun onSearchQuerySubmitted(query: String) {
        if (query.isBlank()) return
        lastSearchQuery = query
        performSearch(query)
    }

    fun onAddButtonClicked(show: DiscoverResultViewModel) {
        show.isAddInProgress = true
        view?.updateSearchResult(show)

        launch {
            myShowsRepository.addShow(show.id)
            delay(300)
            show.isInMyShows = true
            show.isAddInProgress = false
            view?.updateSearchResult(show)
        }
    }

    fun onRemoveButtonClicked(show: DiscoverResultViewModel) {
        show.isAddInProgress = true
        view?.updateSearchResult(show)

        launch {
            myShowsRepository.removeShow(show.id)
            delay(300)
            show.isInMyShows = false
            show.isAddInProgress = false
            view?.updateSearchResult(show)
        }
    }

    fun onShowClicked(show: DiscoverResultViewModel) {
        view?.openDiscoverShow(show)
    }

    fun onRetryButtonClicked() {
        lastSearchQuery ?: return
        performSearch(lastSearchQuery!!)
    }

    private fun performSearch(query: String) {
        view?.hidePrompt()
        view?.hideEmptyMessage()
        view?.hideError()
        view?.showProgress()

        launch {
            try {
                val results = discoverRepository.search(query).map { result ->
                    val isInMyShow = myShowsRepository.isAddedOrAddingToMyShows(result.tmdbId)
                    return@map DiscoverResultViewModel.map(result, isInMyShow)
                }

                searchResults = results
                view?.showSearchResults(results)
                if (results.isEmpty()) {
                    view?.showEmptyMessage()
                }
            } catch (error: Throwable) {
                searchResults = null
                view?.showSearchResults(emptyList())
                view?.showError()
            } finally {
                view?.hideProgress()
            }
        }
    }
}
