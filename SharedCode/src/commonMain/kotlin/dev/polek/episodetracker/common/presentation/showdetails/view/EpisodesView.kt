package dev.polek.episodetracker.common.presentation.showdetails.view

import dev.polek.episodetracker.common.presentation.showdetails.model.SeasonViewModel

interface EpisodesView {

    fun displaySeasons(seasons: List<SeasonViewModel>)
}
