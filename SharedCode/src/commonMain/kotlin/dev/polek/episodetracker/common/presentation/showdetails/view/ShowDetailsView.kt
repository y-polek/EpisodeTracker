package dev.polek.episodetracker.common.presentation.showdetails.view

import dev.polek.episodetracker.common.presentation.showdetails.model.ShowHeaderViewModel

interface ShowDetailsView {

    fun displayShowHeader(show: ShowHeaderViewModel)
    fun close()
}
