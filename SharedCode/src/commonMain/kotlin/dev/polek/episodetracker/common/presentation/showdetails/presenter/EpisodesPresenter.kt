package dev.polek.episodetracker.common.presentation.showdetails.presenter

import dev.polek.episodetracker.common.presentation.BasePresenter
import dev.polek.episodetracker.common.presentation.showdetails.model.EpisodeViewModel
import dev.polek.episodetracker.common.presentation.showdetails.model.SeasonViewModel
import dev.polek.episodetracker.common.presentation.showdetails.view.EpisodesView
import dev.polek.episodetracker.common.repositories.EpisodesRepository

class EpisodesPresenter(
    private val showId: Int,
    private val repository: EpisodesRepository) : BasePresenter<EpisodesView>()
{

    override fun attachView(view: EpisodesView) {
        super.attachView(view)
        view.displaySeasons(repository.allSeasons(showTmdbId = showId))
    }

    fun onEpisodeWatchedStateChanged(episode: EpisodeViewModel) {
        repository.setEpisodeWatched(
            showTmdbId = showId,
            seasonNumber = episode.season,
            episodeNumber = episode.number,
            isWatched = episode.isWatched
        )
    }

    fun onSeasonWatchedStateChanged(season: SeasonViewModel) {
        repository.setSeasonWatched(
            showTmdbId = showId,
            seasonNumber = season.number,
            isWatched = season.isWatched)
    }
}
