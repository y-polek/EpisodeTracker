package dev.polek.episodetracker.common.presentation.showdetails.presenter

import dev.polek.episodetracker.common.datasource.db.QueryListener.Subscriber
import dev.polek.episodetracker.common.datasource.themoviedb.TmdbService
import dev.polek.episodetracker.common.datasource.themoviedb.TmdbService.Companion.backdropImageUrl
import dev.polek.episodetracker.common.datasource.themoviedb.entities.ShowDetailsEntity
import dev.polek.episodetracker.common.logging.log
import dev.polek.episodetracker.common.logging.loge
import dev.polek.episodetracker.common.model.EpisodeNumber
import dev.polek.episodetracker.common.model.Season
import dev.polek.episodetracker.common.presentation.BasePresenter
import dev.polek.episodetracker.common.presentation.showdetails.model.*
import dev.polek.episodetracker.common.presentation.showdetails.view.ShowDetailsView
import dev.polek.episodetracker.common.repositories.EpisodesRepository
import dev.polek.episodetracker.common.repositories.MyShowsRepository
import dev.polek.episodetracker.common.repositories.ShowRepository
import dev.polek.episodetracker.db.ShowDetails
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ShowDetailsPresenter(
    private val showId: Int,
    private val myShowsRepository: MyShowsRepository,
    private val showRepository: ShowRepository,
    private val episodesRepository: EpisodesRepository) : BasePresenter<ShowDetailsView>()
{
    private var showDetails: ShowDetailsEntity? = null
    private var showSeasons: List<Season>? = null
    private var seasonsViewModel: SeasonsViewModel = SeasonsViewModel(emptyList())
    private var episodesTabRevealed = false

    private val isShowAddedOrAddingSubscriber = object : Subscriber<Boolean> {
        @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
        override fun onQueryResult(isAddedOrAdding: Boolean) {
            if (isAddedOrAdding) {
                view?.displayAddToMyShowsProgress()
                launch {
                    delay(500)
                    view?.hideAddToMyShowsButton()
                }
            } else {
                view?.displayAddToMyShowsButton()
            }
        }
    }

    override fun attachView(view: ShowDetailsView) {
        super.attachView(view)

        loadShowDetails()
    }

    override fun onViewAppeared() {
        super.onViewAppeared()
        myShowsRepository.setIsAddedOrAddingToMyShowsSubscriber(showId, isShowAddedOrAddingSubscriber)
    }

    override fun onViewDisappeared() {
        myShowsRepository.removeIsAddedOrAddingToMyShowsSubscriber(showId)
        super.onViewDisappeared()
    }

    fun onEpisodesTabSelected() {
        if (episodesTabRevealed) return

        episodesTabRevealed = true

        val inDb = myShowsRepository.isAddedToMyShows(showId)
        if (inDb) {
            loadEpisodesFromDb()
        } else {
            if (showDetails != null) {
                loadEpisodesFromNetwork()
            } else {
                view?.hideEpisodesProgress()
                view?.showEpisodesError()
            }
        }
    }

    fun onMenuClicked() {
        val isAddedOrAdding = myShowsRepository.isAddedOrAddingToMyShows(showId)
        view?.displayOptionsMenu(isInMyShows = isAddedOrAdding)
    }

    fun onAddToMyShowsClicked() {
        view?.displayAddToMyShowsProgress()
        launch {
            myShowsRepository.addShow(showId)
        }
    }

    fun onShareShowClicked() {

    }

    fun onMarkWatchedClicked() {
        TODO("not implemented")
    }

    fun onRemoveShowClicked() {
        myShowsRepository.removeShow(showId)
    }

    fun onArchiveShowClicked() {
        TODO("not implemented")
    }

    fun onRetryButtonClicked() {
        loadShowDetails()
    }

    fun onRecommendationClicked(recommendation: RecommendationViewModel) {
        view?.openRecommendation(recommendation)
    }

    fun onEpisodeWatchedStateToggled(episode: EpisodeViewModel) {
        val inDb = myShowsRepository.isAddedToMyShows(showId)

        if (inDb) {
            processEpisodeWatchedStateToggle(episode)
        } else {
            val show = checkNotNull(showDetails) { "`showDetails` must not be null at this moment" }
            val seasons = checkNotNull(showSeasons) { "`showSeasons` must not be null at this moment" }
            
            view?.displayAddToMyShowsConfirmation(showName = show.name.orEmpty()) { confirmed ->
                if (confirmed) {
                    showRepository.writeShowToDb(show, seasons)
                    processEpisodeWatchedStateToggle(episode)
                } else {
                    view?.reloadSeason(episode.number.season)
                }
            }
        }
    }

    fun onSeasonWatchedStateToggled(season: SeasonViewModel) {
        val inDb = myShowsRepository.isAddedToMyShows(showId)

        if (inDb) {
            processSeasonWatchedStateToggled(season)
        } else {
            val show = checkNotNull(showDetails) { "`showDetails` must not be null at this moment" }
            val seasons = checkNotNull(showSeasons) { "`showSeasons` must not be null at this moment" }

            view?.displayAddToMyShowsConfirmation(showName = show.name.orEmpty()) { confirmed ->
                if (confirmed) {
                    showRepository.writeShowToDb(show, seasons)
                    processSeasonWatchedStateToggled(season)
                } else {
                    view?.reloadSeason(season.number)
                }
            }
        }
    }

    fun onEpisodesRetryButtonClicked() {
        if (showDetails != null) {
            loadEpisodesFromNetwork()
        } else {
            loadShowDetails()
        }
    }

    private fun loadShowDetails() {
        view?.showProgress()
        view?.hideError()

        val inDb = myShowsRepository.isAddedToMyShows(showId)
        if (inDb) {
            val show = myShowsRepository.showDetails(showId)
            checkNotNull(show) { "Can't find show with $showId ID in My Shows" }
            displayHeader(show)
            displayDetails(show)
            loadEpisodesFromDb()
            view?.hideProgress()
        } else {
            view?.showEpisodesProgress()
        }

        launch {
            try {
                val show = showRepository.showDetails(showId)
                showDetails = show
                if (!inDb) {
                    displayHeader(show)
                    displayDetails(show)
                }
                displayAdditionalInfo(show)

                val imdbId = show.externalIds?.imdbId
                if (imdbId != null) {
                    loadImdbRating(imdbId)
                }
            } catch (e: Throwable) {
                if (!inDb) {
                    view?.showError()
                }
            } finally {
                view?.hideProgress()
            }

            if (episodesTabRevealed) {
                if (!inDb) {
                    if (showDetails != null) {
                        loadEpisodesFromNetwork()
                    } else {
                        view?.hideEpisodesProgress()
                        view?.showEpisodesError()
                    }
                }
            }
        }
    }

    private fun displayHeader(show: ShowDetails) {
        val headerViewModel = ShowHeaderViewModel(
            name = show.name,
            imageUrl = show.imageUrl,
            rating = show.contentRating.orEmpty(),
            year = show.year,
            endYear = if (show.isEnded) show.lastYear else null,
            networks = show.networks)
        view?.displayShowHeader(headerViewModel)
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

    private fun displayDetails(show: ShowDetails) {
        val detailsViewModel = ShowDetailsViewModel(
            overview = show.overview,
            genres = show.genres,
            homePageUrl = show.homePageUrl,
            imdbId = show.imdbId,
            instagramUsername = show.instagramId,
            facebookProfile = show.facebookId,
            twitterUsername = show.twitterId)
        view?.displayShowDetails(detailsViewModel)
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
                log { "Failed to load IMDB rating: $e" }
            }
        }
    }

    private fun loadEpisodesFromDb() {
        val seasonsList = episodesRepository.allSeasons(showId).map(SeasonViewModel.Companion::map)
        seasonsViewModel = SeasonsViewModel(seasonsList)
        view?.displayEpisodes(seasonsList)
        view?.hideEpisodesProgress()
    }

    private fun loadEpisodesFromNetwork() {
        val numberOfSeasons = showDetails?.numberOfSeasons
        if (numberOfSeasons == null) {
            loge { "Can't load episodes list without ShowDetails" }
            return
        }

        view?.showEpisodesProgress()
        view?.hideEpisodesError()

        launch {
            try {
                showSeasons = (1..numberOfSeasons)
                    .mapNotNull { seasonNumber ->
                        showRepository.season(showTmdbId = showId, seasonNumber = seasonNumber)
                    }
                val seasonsList = showSeasons!!.map(SeasonViewModel.Companion::map)
                seasonsViewModel = SeasonsViewModel(seasonsList)
                view?.displayEpisodes(seasonsList)
            } catch (e: Throwable) {
                view?.showEpisodesError()
            } finally {
                view?.hideEpisodesProgress()
            }
        }
    }

    private fun processEpisodeWatchedStateToggle(episode: EpisodeViewModel) {
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

    private fun processSeasonWatchedStateToggled(season: SeasonViewModel) {
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
        seasonsViewModel.get(1 until episodeNumber.season).forEach { season ->
            season.isWatched = true
        }

        seasonsViewModel[episodeNumber.season]?.episodes?.toEpisodes()?.get(1..episodeNumber.episode)?.forEach { episode ->
            episode.isWatched = true
        }

        episodesRepository.markAllWatchedUpTo(episodeNumber = episodeNumber, showTmdbId = showId)

        (1..episodeNumber.season).forEach { season ->
            view?.reloadSeason(season)
        }
    }

    private fun markAllSeasonsWatchedUpTo(seasonNumber: Int) {
        seasonsViewModel.get(1..seasonNumber).forEach { season ->
            season.isWatched = true
        }

        episodesRepository.markAllWatchedUpToSeason(season = seasonNumber, showTmdbId = showId)

        (1..seasonNumber).forEach { season ->
            view?.reloadSeason(season)
        }
    }
}
