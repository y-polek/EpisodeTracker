package dev.polek.episodetracker.common.presentation.myshows.model

class MyShowsViewModel(
    var isUpcomingExpanded: Boolean = true,
    var isToBeAnnouncedExpanded: Boolean = true,
    var isEndedExpanded: Boolean = true,
    var isArchivedExpanded: Boolean = true)
{
    fun toggleUpcomingExpanded() {
        isUpcomingExpanded = !isUpcomingExpanded
    }

    fun toggleToBeAnnouncedExpanded() {
        isToBeAnnouncedExpanded = !isToBeAnnouncedExpanded
    }

    fun toggleEndedExpanded() {
        isEndedExpanded = !isEndedExpanded
    }

    fun toggleArchivedExpanded() {
        isArchivedExpanded = !isArchivedExpanded
    }
}
