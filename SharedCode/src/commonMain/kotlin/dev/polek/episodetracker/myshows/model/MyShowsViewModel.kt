package dev.polek.episodetracker.myshows.model

import dev.polek.episodetracker.myshows.model.MyShowsListItem.*

class MyShowsViewModel(
    private val upcomingShows: List<UpcomingShowViewModel>,
    private val toBeAnnouncedShows: List<ShowViewModel>,
    private val endedShows: List<ShowViewModel>,
    isUpcomingExpanded: Boolean,
    isToBeAnnouncedExpanded: Boolean,
    isEndedExpanded: Boolean)
{
    val items: List<MyShowsListItem> = populateItemList(
        isUpcomingExpanded = isUpcomingExpanded,
        isToBeAnnouncedExpanded = isToBeAnnouncedExpanded,
        isEndedExpanded = isEndedExpanded)

    private fun populateItemList(
        isUpcomingExpanded: Boolean,
        isToBeAnnouncedExpanded: Boolean,
        isEndedExpanded: Boolean): List<MyShowsListItem>
    {
        val items = mutableListOf<MyShowsListItem>()
        if (upcomingShows.isNotEmpty()) {
            // "Upcoming" Group Header
            items.add(GroupViewModel("Upcoming", isUpcomingExpanded))

            // "Upcoming" Shows
            if (isUpcomingExpanded) {
                items.addAll(upcomingShows)
            }
        }
        if (toBeAnnouncedShows.isNotEmpty()) {
            // "To Be Announced" Group Header
            items.add(GroupViewModel("To Be Announced", isToBeAnnouncedExpanded))

            // "To Be Announced" Shows
            if (isToBeAnnouncedExpanded) {
                items.addAll(toBeAnnouncedShows)
            }
        }
        if (endedShows.isNotEmpty()) {
            // "Ended" Group Header
            items.add(GroupViewModel("Ended", isEndedExpanded))

            // "Ended" Shows
            if (isEndedExpanded) {
                items.addAll(endedShows)
            }
        }
        return items
    }
}
