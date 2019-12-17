package dev.polek.episodetracker.discover

import dev.polek.episodetracker.common.presentation.BasePresenter
import dev.polek.episodetracker.discover.model.DiscoverResultViewModel
import kotlinx.coroutines.launch

class DiscoverPresenter(private val repository: DiscoverRepository) : BasePresenter<DiscoverView>() {

    override fun attachView(view: DiscoverView) {
        super.attachView(view)

        view.showPrompt()
    }

    fun onSearchQuerySubmitted(query: String) {
        view?.hidePrompt()
        view?.hideEmptyMessage()
        view?.showProgress()

        launch {
            val results = repository.search(query).map {
                DiscoverResultViewModel(
                    id = it.id,
                    name = it.name,
                    year = it.year?.toString() ?: "",
                    posterUrl = it.posterUrl,
                    overview = it.overview,
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
}
