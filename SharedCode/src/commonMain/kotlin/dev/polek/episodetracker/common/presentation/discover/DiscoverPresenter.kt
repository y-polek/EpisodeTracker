package dev.polek.episodetracker.common.presentation.discover

import dev.polek.episodetracker.common.analytics.Analytics
import dev.polek.episodetracker.common.analytics.Analytics.Screen
import dev.polek.episodetracker.common.di.Inject
import dev.polek.episodetracker.common.di.Singleton
import dev.polek.episodetracker.common.presentation.BasePresenter
import dev.polek.episodetracker.common.presentation.discover.model.DiscoverResultViewModel
import dev.polek.episodetracker.common.repositories.DiscoverRepository
import dev.polek.episodetracker.common.repositories.MyShowsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Singleton
class DiscoverPresenter @Inject constructor(
    private val discoverRepository: DiscoverRepository,
    private val myShowsRepository: MyShowsRepository,
    private val analytics: Analytics) : BasePresenter<DiscoverView>()
{
    private var searchResults: List<DiscoverResultViewModel>? = null

    private var lastSearchQuery: String? = null

    override fun attachView(view: DiscoverView) {
        super.attachView(view)
        if (searchResults == null) {
            view.showPrompt()
        } else {
            view.showSearchResults(searchResults!!)
        }
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

        analytics.logAddShow(show.id, Screen.DISCOVER)
    }

    fun onRemoveButtonClicked(show: DiscoverResultViewModel) {
        view?.displayRemoveShowConfirmation(show) { confirmed ->
            if (!confirmed) return@displayRemoveShowConfirmation

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

        analytics.logRemoveShow(show.id, Screen.DISCOVER)
    }

    fun onShowClicked(show: DiscoverResultViewModel) {
        view?.openDiscoverShow(show)
        analytics.logOpenDetails(show.id, Screen.DISCOVER)
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
