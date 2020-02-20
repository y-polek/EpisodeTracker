package dev.polek.episodetracker.common.presentation.showdetails.view

import dev.polek.episodetracker.common.presentation.showdetails.model.CastMemberViewModel
import dev.polek.episodetracker.common.presentation.showdetails.model.RecommendationViewModel
import dev.polek.episodetracker.common.presentation.showdetails.model.ShowDetailsViewModel
import dev.polek.episodetracker.common.presentation.showdetails.model.TrailerViewModel

interface AboutShowView {

    fun displayShowDetails(show: ShowDetailsViewModel)
    fun displayTrailers(trailers: List<TrailerViewModel>)
    fun displayCast(cast: List<CastMemberViewModel>)
    fun displayRecommendations(recommendations: List<RecommendationViewModel>)
    fun displayImdbRating(rating: Float)
}
