package dev.polek.episodetracker.common.presentation.showdetails

import dev.polek.episodetracker.common.presentation.showdetails.model.ShowDetailsViewModel

interface ShowDetailsView {

    fun displayShowDetails(show: ShowDetailsViewModel)
    fun close()
}
