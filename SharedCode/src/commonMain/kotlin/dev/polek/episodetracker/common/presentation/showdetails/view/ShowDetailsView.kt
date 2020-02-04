package dev.polek.episodetracker.common.presentation.showdetails.view

import dev.polek.episodetracker.common.presentation.showdetails.model.ShowDetailsViewModel

interface ShowDetailsView {

    fun displayShowDetails(show: ShowDetailsViewModel)
    fun close()
}
