package dev.polek.episodetracker.common.presentation.showdetails.presenter

import dev.polek.episodetracker.common.model.EpisodeNumber
import dev.polek.episodetracker.common.presentation.BasePresenter
import dev.polek.episodetracker.common.presentation.showdetails.model.EpisodeViewModel
import dev.polek.episodetracker.common.presentation.showdetails.model.SeasonViewModel
import dev.polek.episodetracker.common.presentation.showdetails.model.SeasonsViewModel
import dev.polek.episodetracker.common.presentation.showdetails.model.toEpisodes
import dev.polek.episodetracker.common.presentation.showdetails.view.EpisodesView
import dev.polek.episodetracker.common.repositories.EpisodesRepository

class EpisodesPresenter(
    private val showId: Int,
    private val repository: EpisodesRepository) : BasePresenter<EpisodesView>()
{

    private var seasons: SeasonsViewModel = SeasonsViewModel(emptyList())

    override fun attachView(view: EpisodesView) {
        super.attachView(view)
        seasons = SeasonsViewModel(repository.allSeasons(showTmdbId = showId))
        view.displaySeasons(seasons.asList())
    }

    fun onEpisodeWatchedStateToggled(episode: EpisodeViewModel) {
        val isWatched = !episode.isWatched

        val firstNotWatchedNumber = repository.firstNotWatchedEpisode(showId)
        if (isWatched && firstNotWatchedNumber != null && firstNotWatchedNumber < episode.number) {
            view?.showCheckAllPreviousEpisodesPrompt { checkAllPreviousEpisodes ->
                if (checkAllPreviousEpisodes) {
                    markAllEpisodesWatchedUpTo(episode.number)
                } else {
                    setEpisodeWatched(episode, isWatched)
                }
            }
        } else {
            setEpisodeWatched(episode, isWatched)
        }
    }

    fun onSeasonWatchedStateToggled(season: SeasonViewModel) {
        val isWatched = !season.isWatched

        val firstNotWatchedSeason = repository.firstNotWatchedEpisode(showId)?.season ?: season.number
        if (isWatched && firstNotWatchedSeason < season.number) {
            view?.showCheckAllPreviousEpisodesPrompt { checkAllPreviousEpisodes ->
                if (checkAllPreviousEpisodes) {
                    markAllSeasonsWatchedUpTo(season.number)
                } else {
                    setSeasonWatched(season, isWatched)
                }
            }
        } else {
            setSeasonWatched(season, isWatched)
        }
    }

    private fun setEpisodeWatched(episode: EpisodeViewModel, isWatched: Boolean) {
        episode.isWatched = isWatched

        repository.setEpisodeWatched(
            showTmdbId = showId,
            seasonNumber = episode.number.season,
            episodeNumber = episode.number.episode,
            isWatched = isWatched)

        view?.reloadSeason(episode.number.season)
    }

    private fun setSeasonWatched(season: SeasonViewModel, isWatched: Boolean) {
        season.isWatched = isWatched

        repository.setSeasonWatched(
            showTmdbId = showId,
            seasonNumber = season.number,
            isWatched = isWatched)

        view?.reloadSeason(season.number)
    }

    private fun markAllEpisodesWatchedUpTo(episodeNumber: EpisodeNumber) {
        seasons.get(1 until episodeNumber.season).forEach { season ->
            season.isWatched = true
        }

        seasons[episodeNumber.season]?.episodes?.toEpisodes()?.get(1..episodeNumber.episode)?.forEach { episode ->
            episode.isWatched = true
        }

        repository.markAllWatchedUpTo(episodeNumber = episodeNumber, showTmdbId = showId)

        (1..episodeNumber.season).forEach { season ->
            view?.reloadSeason(season)
        }
    }

    private fun markAllSeasonsWatchedUpTo(seasonNumber: Int) {
        seasons.get(1..seasonNumber).forEach { season ->
            season.isWatched = true
        }

        repository.markAllWatchedUpToSeason(season = seasonNumber, showTmdbId = showId)

        (1..seasonNumber).forEach { season ->
            view?.reloadSeason(season)
        }
    }
}
