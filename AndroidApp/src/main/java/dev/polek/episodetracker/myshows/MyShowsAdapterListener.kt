package dev.polek.episodetracker.myshows

import dev.polek.episodetracker.common.presentation.myshows.model.MyShowsListItem

interface MyShowsAdapterListener {
    fun onShowClicked(show: MyShowsListItem.ShowViewModel)
}
