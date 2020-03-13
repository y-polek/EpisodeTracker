package dev.polek.episodetracker.common.presentation.myshows

import dev.polek.episodetracker.common.presentation.myshows.model.MyShowsListItem
import dev.polek.episodetracker.common.presentation.myshows.model.MyShowsViewModel

interface MyShowsView {
    fun updateShows(model: MyShowsViewModel)
    fun openMyShowDetails(show: MyShowsListItem.ShowViewModel)
}
