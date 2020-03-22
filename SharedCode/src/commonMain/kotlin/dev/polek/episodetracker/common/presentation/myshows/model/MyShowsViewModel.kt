package dev.polek.episodetracker.common.presentation.myshows.model

import dev.polek.episodetracker.common.presentation.myshows.model.MyShowsListItem.*
import dev.polek.episodetracker.common.presentation.myshows.model.MyShowsListItem.GroupViewModel.*

class MyShowsViewModel(
    var upcomingShows: List<UpcomingShowViewModel>,
    var toBeAnnouncedShows: List<ShowViewModel>,
    var endedShows: List<ShowViewModel>,
    var archivedShows: List<ShowViewModel>,
    isUpcomingExpanded: Boolean,
    isToBeAnnouncedExpanded: Boolean,
    isEndedExpanded: Boolean,
    isArchivedExpanded: Boolean)
{
    var isUpcomingExpanded: Boolean = isUpcomingExpanded
        private set
    var isToBeAnnouncedExpanded: Boolean = isToBeAnnouncedExpanded
        private set
    var isEndedExpanded: Boolean = isEndedExpanded
        private set
    var isArchivedExpanded: Boolean = isArchivedExpanded
        private set

    var items: List<MyShowsListItem> = populateItemList()
        private set

    fun setUpcomingExpanded(expanded: Boolean) {
        isUpcomingExpanded = expanded
        items = populateItemList()
    }

    fun toggleUpcomingExpanded() {
        setUpcomingExpanded(!isUpcomingExpanded)
    }

    fun setToBeAnnouncedExpanded(expanded: Boolean) {
        isToBeAnnouncedExpanded = expanded
        items = populateItemList()
    }

    fun toggleToBeAnnouncedExpanded() {
        setToBeAnnouncedExpanded(!isToBeAnnouncedExpanded)
    }

    fun setEndedExpanded(expanded: Boolean) {
        isEndedExpanded = expanded
        items = populateItemList()
    }

    fun toggleEndedExpanded() {
        setEndedExpanded(!isEndedExpanded)
    }

    fun setArchivedExpanded(expanded: Boolean) {
        isArchivedExpanded = expanded
        items = populateItemList()
    }

    fun toggleArchivedExpanded() {
        setArchivedExpanded(!isArchivedExpanded)
    }

    private fun populateItemList(): List<MyShowsListItem> {
        val items = mutableListOf<MyShowsListItem>()
        if (upcomingShows.isNotEmpty()) {
            // "Upcoming" Group Header
            items.add(UpcomingGroupViewModel("Upcoming", isUpcomingExpanded))

            // "Upcoming" Shows
            if (isUpcomingExpanded) {
                items.addAll(upcomingShows)
            }
        }
        if (toBeAnnouncedShows.isNotEmpty()) {
            // "To Be Announced" Group Header
            items.add(ToBeAnnouncedGroupViewModel("To Be Announced", isToBeAnnouncedExpanded))

            // "To Be Announced" Shows
            if (isToBeAnnouncedExpanded) {
                items.addAll(toBeAnnouncedShows)
            }
        }
        if (endedShows.isNotEmpty()) {
            // "Ended" Group Header
            items.add(EndedGroupViewModel("Ended", isEndedExpanded))

            // "Ended" Shows
            if (isEndedExpanded) {
                items.addAll(endedShows)
            }
        }
        if (archivedShows.isNotEmpty()) {
            // "Archived" Group Header
            items.add(ArchivedGroupViewModel("Archived", isArchivedExpanded))

            // "Archived" Shows
            if (isArchivedExpanded) {
                items.addAll(archivedShows)
            }
        }
        return items
    }

    companion object {
        val EMPTY =
            MyShowsViewModel(
                upcomingShows = emptyList(),
                toBeAnnouncedShows = emptyList(),
                endedShows = emptyList(),
                archivedShows = emptyList(),
                isUpcomingExpanded = true,
                isToBeAnnouncedExpanded = true,
                isEndedExpanded = true,
                isArchivedExpanded = true)
    }
}
