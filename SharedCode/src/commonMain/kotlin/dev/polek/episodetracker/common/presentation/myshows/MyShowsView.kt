package dev.polek.episodetracker.common.presentation.myshows

import dev.polek.episodetracker.common.presentation.myshows.model.MyShowsListItem

interface MyShowsView {
    fun displayLastWeekShows(shows: List<MyShowsListItem.UpcomingShowViewModel>, isVisible: Boolean)
    fun displayUpcomingShows(shows: List<MyShowsListItem.UpcomingShowViewModel>)
    fun displayToBeAnnouncedShows(shows: List<MyShowsListItem.ShowViewModel>)
    fun displayEndedShows(shows: List<MyShowsListItem.ShowViewModel>)
    fun displayArchivedShows(shows: List<MyShowsListItem.ShowViewModel>)
    fun showEmptyMessage(isFiltered: Boolean)
    fun hideEmptyMessage()
    fun hideRefresh()
    fun openMyShowDetails(show: MyShowsListItem.ShowViewModel)
}
