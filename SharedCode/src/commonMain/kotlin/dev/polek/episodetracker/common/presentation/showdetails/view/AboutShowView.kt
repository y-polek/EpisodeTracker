package dev.polek.episodetracker.common.presentation.showdetails.view

import dev.polek.episodetracker.common.model.CastMember
import dev.polek.episodetracker.common.model.Trailer
import dev.polek.episodetracker.common.presentation.showdetails.model.ShowDetailsViewModel

interface AboutShowView {

    fun displayShowDetails(show: ShowDetailsViewModel)
    fun displayTrailers(trailers: List<Trailer>)
    fun displayCast(cast: List<CastMember>)
}
