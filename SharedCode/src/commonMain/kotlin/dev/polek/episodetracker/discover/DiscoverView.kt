package dev.polek.episodetracker.discover

import dev.polek.episodetracker.discover.model.DiscoverResultViewModel

interface DiscoverView {
    fun showPrompt()
    fun hidePrompt()
    fun showProgress()
    fun hideProgress()
    fun showSearchResults(results: List<DiscoverResultViewModel>)
    fun updateSearchResult(result: DiscoverResultViewModel)
    fun showEmptyMessage()
    fun hideEmptyMessage()
}
