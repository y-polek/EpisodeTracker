package dev.polek.episodetracker.myshows

import dev.polek.episodetracker.myshows.model.MyShowsViewModel

interface MyShowsView {
    fun updateShows(model: MyShowsViewModel)
}
