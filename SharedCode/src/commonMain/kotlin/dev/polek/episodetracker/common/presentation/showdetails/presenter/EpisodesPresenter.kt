package dev.polek.episodetracker.common.presentation.showdetails.presenter

import dev.polek.episodetracker.common.logging.log
import dev.polek.episodetracker.common.model.EpisodeNumber
import dev.polek.episodetracker.common.presentation.BasePresenter
import dev.polek.episodetracker.common.presentation.showdetails.model.EpisodeViewModel
import dev.polek.episodetracker.common.presentation.showdetails.model.SeasonViewModel
import dev.polek.episodetracker.common.presentation.showdetails.model.SeasonsViewModel
import dev.polek.episodetracker.common.presentation.showdetails.model.toEpisodes
import dev.polek.episodetracker.common.presentation.showdetails.view.EpisodesView
import dev.polek.episodetracker.common.repositories.EpisodesRepository
import dev.polek.episodetracker.common.repositories.MyShowsRepository
import dev.polek.episodetracker.common.repositories.ShowRepository
import kotlinx.coroutines.launch

class EpisodesPresenter(
    private val showId: Int,
    private val myShowsRepository: MyShowsRepository,
    private val episodesRepository: EpisodesRepository,
    private val showRepository: ShowRepository) : BasePresenter<EpisodesView>()
{

    private var seasons: SeasonsViewModel = SeasonsViewModel(emptyList())

    override fun onViewAppeared() {
        super.onViewAppeared()

        log("EpisodesPresenter: onViewAppeared")

        launch {
            loadEpisodes()
        }
    }

    fun onEpisodeWatchedStateToggled(episode: EpisodeViewModel) {
        val isWatched = !episode.isWatched

        val firstNotWatchedNumber = episodesRepository.firstNotWatchedEpisode(showId)
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

        val firstNotWatchedSeason = episodesRepository.firstNotWatchedEpisode(showId)?.season ?: season.number
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

    private suspend fun loadEpisodes() {
        val seasonsList = when {
            myShowsRepository.isInMyShows(showId) -> {
                episodesRepository.allSeasons(showTmdbId = showId)
            }
            else -> {
                val show = showRepository.showDetails(showId)
                (1..show.numberOfSeasons)
                    .mapNotNull { seasonNumber ->
                        val season = showRepository.season(showTmdbId = showId, seasonNumber = seasonNumber)
                            ?: return@mapNotNull null
                        SeasonViewModel.map(season)
                    }
            }
        }

        log("EpisodesPresenter: ${seasonsList.size}")

        seasons = SeasonsViewModel(seasonsList)
        view?.displaySeasons(seasons.asList())
    }

    private fun setEpisodeWatched(episode: EpisodeViewModel, isWatched: Boolean) {
        episode.isWatched = isWatched

        episodesRepository.setEpisodeWatched(
            showTmdbId = showId,
            seasonNumber = episode.number.season,
            episodeNumber = episode.number.episode,
            isWatched = isWatched)

        view?.reloadSeason(episode.number.season)
    }

    private fun setSeasonWatched(season: SeasonViewModel, isWatched: Boolean) {
        season.isWatched = isWatched

        episodesRepository.setSeasonWatched(
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

        episodesRepository.markAllWatchedUpTo(episodeNumber = episodeNumber, showTmdbId = showId)

        (1..episodeNumber.season).forEach { season ->
            view?.reloadSeason(season)
        }
    }

    private fun markAllSeasonsWatchedUpTo(seasonNumber: Int) {
        seasons.get(1..seasonNumber).forEach { season ->
            season.isWatched = true
        }

        episodesRepository.markAllWatchedUpToSeason(season = seasonNumber, showTmdbId = showId)

        (1..seasonNumber).forEach { season ->
            view?.reloadSeason(season)
        }
    }
}
