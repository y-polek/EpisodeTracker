package dev.polek.episodetracker.common.presentation.showdetails.presenter

import dev.polek.episodetracker.common.datasource.themoviedb.TmdbService
import dev.polek.episodetracker.common.datasource.themoviedb.TmdbService.Companion.backdropImageUrl
import dev.polek.episodetracker.common.datasource.themoviedb.entities.ShowDetailsEntity
import dev.polek.episodetracker.common.logging.log
import dev.polek.episodetracker.common.model.EpisodeNumber
import dev.polek.episodetracker.common.presentation.BasePresenter
import dev.polek.episodetracker.common.presentation.showdetails.model.*
import dev.polek.episodetracker.common.presentation.showdetails.view.ShowDetailsView
import dev.polek.episodetracker.common.repositories.EpisodesRepository
import dev.polek.episodetracker.common.repositories.MyShowsRepository
import dev.polek.episodetracker.common.repositories.ShowRepository
import kotlinx.coroutines.launch

class ShowDetailsPresenter(
    private val showId: Int,
    private val myShowsRepository: MyShowsRepository,
    private val showRepository: ShowRepository,
    private val episodesRepository: EpisodesRepository) : BasePresenter<ShowDetailsView>()
{

    private var showDetails: ShowDetailsEntity? = null
    private var seasons: SeasonsViewModel = SeasonsViewModel(emptyList())
    private var episodesTabRevealed = false

    override fun attachView(view: ShowDetailsView) {
        super.attachView(view)

        loadShowDetails()
    }

    fun onEpisodesTabSelected() {
        if (episodesTabRevealed) return

        episodesTabRevealed = true

        if (showDetails != null) {
            launch {
                loadEpisodes(showDetails!!)
            }
        }
    }

    fun onAddToMyShowsButtonClicked() {
        view?.displayAddToMyShowsProgress()
        launch {
            myShowsRepository.addShow(showId)
            view?.hideAddToMyShowsButton()
        }
    }

    fun onRetryButtonClicked() {
        loadShowDetails()
    }

    fun onRecommendationClicked(recommendation: RecommendationViewModel) {
        view?.openRecommendation(recommendation)
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

    fun onEpisodesRetryButtonClicked() {
        showDetails ?: return
        launch {
            loadEpisodes(showDetails!!)
        }
    }

    private fun loadShowDetails() {
        view?.showProgress()
        view?.hideError()

        val isInMyShows = myShowsRepository.isInMyShows(showId)
        if (isInMyShows) {
            val show = myShowsRepository.showDetails(showId)
            checkNotNull(show) { "Can't find show with $showId ID in My Shows" }
            val headerViewModel = ShowHeaderViewModel(
                name = show.name,
                imageUrl = show.imageUrl,
                rating = show.contentRating.orEmpty(),
                year = show.year,
                endYear = if (show.isEnded) show.lastYear else null,
                networks = show.networks)
            view?.displayShowHeader(headerViewModel)
            view?.hideProgress()
        }

        launch {
            try {
                val show = showRepository.showDetails(showId)
                showDetails = show
                if (!isInMyShows) {
                    displayHeader(show)
                }
                displayDetails(show)
                displayAdditionalInfo(show)

                val imdbId = show.externalIds?.imdbId
                if (imdbId != null) {
                    loadImdbRating(imdbId)
                }

                if (episodesTabRevealed) {
                    loadEpisodes(show)
                }
            } catch (e: Throwable) {
                view?.showError()
            } finally {
                view?.hideProgress()
            }
        }

        if (isInMyShows) {
            view?.hideAddToMyShowsButton()
        } else {
            view?.displayAddToMyShowsButton()
        }
    }

    private fun displayHeader(show: ShowDetailsEntity) {
        val headerViewModel = ShowHeaderViewModel(
            name = show.name.orEmpty(),
            imageUrl = show.backdropPath?.let(::backdropImageUrl),
            rating = show.contentRating.orEmpty(),
            year = show.year,
            endYear = if (show.isEnded) show.lastYear else null,
            networks = show.networks
        )
        view?.displayShowHeader(headerViewModel)
    }

    private fun displayDetails(show: ShowDetailsEntity) {
        val detailsViewModel = ShowDetailsViewModel(
            overview = show.overview.orEmpty(),
            genres = show.genres,
            homePageUrl = show.homePageUrl,
            imdbId = show.externalIds?.imdbId,
            instagramUsername = show.externalIds?.instagramId,
            facebookProfile = show.externalIds?.facebookId,
            twitterUsername = show.externalIds?.twitterId)
        view?.displayShowDetails(detailsViewModel)
    }

    private fun displayAdditionalInfo(show: ShowDetailsEntity) {
        val trailers = show.videos.map { video ->
            TrailerViewModel(
                name = video.name.orEmpty(),
                youtubeKey = video.key.orEmpty())
        }
        val castMembers = show.cast.map { member ->
            CastMemberViewModel(
                name = member.name.orEmpty(),
                character = member.character.orEmpty(),
                portraitImageUrl = member.profilePath?.let(TmdbService.Companion::profileImageUrl))
        }
        val recommendations = show.recommendations.map { recommendation ->
            RecommendationViewModel(
                showId = recommendation.tmdbId ?: 0,
                name = recommendation.name.orEmpty(),
                imageUrl = recommendation.backdropPath?.let(::backdropImageUrl),
                year = recommendation.year,
                networks = recommendation.networks)
        }

        view?.displayTrailers(trailers)
        view?.displayCast(castMembers)
        view?.displayRecommendations(recommendations)
    }

    private suspend fun loadImdbRating(imdbId: String) {
        launch {
            try {
                val imdbRating = showRepository.imdbRating(imdbId)
                if (imdbRating != null) {
                    view?.displayImdbRating(imdbRating)
                }
            } catch (e: Throwable) {
                log("Failed to load IMDB rating: $e")
            }
        }
    }

    private suspend fun loadEpisodes(show: ShowDetailsEntity) {
        view?.showEpisodesProgress()
        view?.hideEpisodesError()

        val isInMyShow = myShowsRepository.isInMyShows(showId)

        try {
            val seasonsList = when {
                isInMyShow -> episodesRepository.allSeasons(showTmdbId = showId)
                else -> {
                    (1..show.numberOfSeasons)
                        .mapNotNull { seasonNumber ->
                            showRepository.season(showTmdbId = showId, seasonNumber = seasonNumber)
                        }
                }
            }.map(SeasonViewModel.Companion::map)

            seasons = SeasonsViewModel(seasonsList)
            view?.displayEpisodes(seasons.asList())
        } catch (e: Throwable) {
            view?.showEpisodesError()
        } finally {
            view?.hideEpisodesProgress()
        }
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
