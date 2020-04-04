package dev.polek.episodetracker.common.presentation.myshows

import dev.polek.episodetracker.common.presentation.myshows.model.MyShowsListItem
import dev.polek.episodetracker.common.presentation.myshows.model.MyShowsViewModel

interface MyShowsView {
    fun displayUpcomingShows(model: MyShowsViewModel)
    fun displayToBeAnnouncedShows(model: MyShowsViewModel)
    fun displayEndedShows(model: MyShowsViewModel)
    fun displayArchivedShows(model: MyShowsViewModel)
    fun openMyShowDetails(show: MyShowsListItem.ShowViewModel)
}
