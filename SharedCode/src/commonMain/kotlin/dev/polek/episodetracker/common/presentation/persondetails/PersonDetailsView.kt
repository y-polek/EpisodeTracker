package dev.polek.episodetracker.common.presentation.persondetails

import dev.polek.episodetracker.common.presentation.showdetails.model.RecommendationViewModel

interface PersonDetailsView {

    fun displayPersonDetails(person: PersonViewModel)
    fun openShow(show: RecommendationViewModel)
}
