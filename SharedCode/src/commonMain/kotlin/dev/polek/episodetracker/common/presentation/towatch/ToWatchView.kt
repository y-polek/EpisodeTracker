package dev.polek.episodetracker.common.presentation.towatch

import dev.polek.episodetracker.common.model.EpisodeNumber

interface ToWatchView {

    fun displayShows(shows: List<ToWatchShowViewModel>)
    fun showEmptyMessage(isFiltered: Boolean)
    fun hideEmptyMessage()
    fun openToWatchShowDetails(show: ToWatchShowViewModel, episode: EpisodeNumber)
}
