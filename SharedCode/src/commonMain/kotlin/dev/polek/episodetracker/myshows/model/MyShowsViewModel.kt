package dev.polek.episodetracker.myshows.model

import dev.polek.episodetracker.myshows.model.MyShowsListItem.*
import dev.polek.episodetracker.myshows.model.MyShowsListItem.GroupViewModel.*

class MyShowsViewModel(
    private val upcomingShows: List<UpcomingShowViewModel>,
    private val toBeAnnouncedShows: List<ShowViewModel>,
    private val endedShows: List<ShowViewModel>,
    private var isUpcomingExpanded: Boolean,
    private var isToBeAnnouncedExpanded: Boolean,
    private var isEndedExpanded: Boolean)
{
    var items: List<MyShowsListItem> = populateItemList()

    fun setUpcomingExpanded(expanded: Boolean) {
        isUpcomingExpanded = expanded
        items = populateItemList()
    }

    fun setToBeAnnouncedExpanded(expanded: Boolean) {
        isToBeAnnouncedExpanded = expanded
        items = populateItemList()
    }

    fun setEndedExpanded(expanded: Boolean) {
        isEndedExpanded = expanded
        items = populateItemList()
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
        return items
    }
}
