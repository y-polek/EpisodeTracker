package dev.polek.episodetracker.common.presentation.towatch

interface ToWatchView {

    fun displayShows(shows: List<ToWatchShowViewModel>)
    fun updateShow(show: ToWatchShowViewModel)
    fun removeShow(show: ToWatchShowViewModel)
}
