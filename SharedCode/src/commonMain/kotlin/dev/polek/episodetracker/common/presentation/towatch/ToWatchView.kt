package dev.polek.episodetracker.common.presentation.towatch

interface ToWatchView {

    fun displayShows(shows: List<ToWatchShowViewModel>)
    fun showEmptyMessage(isFiltered: Boolean)
    fun hideEmptyMessage()
    fun openToWatchShowDetails(show: ToWatchShowViewModel)
}
