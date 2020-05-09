package dev.polek.episodetracker.common.presentation.showdetails.presenter

import dev.polek.episodetracker.common.datasource.db.QueryListener.Subscriber
import dev.polek.episodetracker.common.datasource.themoviedb.TmdbService
import dev.polek.episodetracker.common.datasource.themoviedb.TmdbService.Companion.backdropImageUrl
import dev.polek.episodetracker.common.datasource.themoviedb.entities.ShowDetailsEntity
import dev.polek.episodetracker.common.logging.log
import dev.polek.episodetracker.common.logging.loge
import dev.polek.episodetracker.common.model.EpisodeNumber
import dev.polek.episodetracker.common.model.Season
import dev.polek.episodetracker.common.preferences.Preferences
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
    private val episodesRepository: EpisodesRepository,
    private val preferences: Preferences) : BasePresenter<ShowDetailsView>()
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

    private val isShowAddedSubscriber = object : Subscriber<Boolean> {
        @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
        override fun onQueryResult(isAdded: Boolean) {
            if (isAdded) {
                loadEpisodesFromDb()
            }
        }
    }

    private val isArchivedSubscriber = object : Subscriber<Boolean> {
        @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
        override fun onQueryResult(isArchived: Boolean) {
            if (isArchived) {
                view?.displayArchivedBadge()
            } else {
                view?.hideArchivedBadge()
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
        myShowsRepository.setIsAddedToMyShowsSubscriber(showId, isShowAddedSubscriber)
        myShowsRepository.setIsArchivedSubscriber(showId, isArchivedSubscriber)
    }

    override fun onViewDisappeared() {
        myShowsRepository.removeIsAddedOrAddingToMyShowsSubscriber(showId)
        myShowsRepository.removeIsAddedToMyShowsSubscriber(showId)
        myShowsRepository.removeIsArchivedSubscriber(showId)
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
        val isArchived = myShowsRepository.isArchived(showId)
        view?.displayOptionsMenu(isInMyShows = isAddedOrAdding, isArchived = isArchived)
    }

    fun onAddToMyShowsClicked() {
        view?.displayAddToMyShowsProgress()
        launch {
            myShowsRepository.addShow(showId)
        }
    }

    fun onShareShowClicked() {
        val inDb = myShowsRepository.isAddedToMyShows(showId)
        val text = if (inDb) {
            val show = myShowsRepository.showDetails(showId) ?: return
            buildShareText(
                name = show.name,
                year = show.year,
                imdbId = show.imdbId,
                homePageUrl = show.homePageUrl)
        } else {
            val show = showDetails ?: return
            buildShareText(
                name = show.name.orEmpty(),
                year = show.year,
                imdbId = show.externalIds?.imdbId,
                homePageUrl = show.homePageUrl)
        }

        view?.shareText(text)
    }

    fun onMarkWatchedClicked() {
        val inDb = myShowsRepository.isAddedToMyShows(showId)
        if (inDb) {
            episodesRepository.markAllWatched(showId)
        } else {
            myShowsRepository.removeShow(showId)
            myShowsRepository.addShow(showId, markAllEpisodesWatched = true)
        }
    }

    fun onRemoveShowClicked() {
        myShowsRepository.removeShow(showId)
    }

    fun onArchiveShowClicked() {
        val inDb = myShowsRepository.isAddedToMyShows(showId)
        if (inDb) {
            myShowsRepository.archiveShow(showId)
        } else {
            myShowsRepository.removeShow(showId)
            myShowsRepository.addShow(showId, archive = true)
        }
    }

    fun onUnarchiveShowClicked() {
        val inDb = myShowsRepository.isAddedToMyShows(showId)
        if (inDb) {
            myShowsRepository.unarchiveShow(showId)
        } else {
            myShowsRepository.removeShow(showId)
            myShowsRepository.addShow(showId, archive = false)
        }
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

    fun onRefreshRequested() {
        val inDb = myShowsRepository.isAddedToMyShows(showId)
        if (inDb) {
            launch {
                showRepository.refreshShow(showId)
                loadShowDetails()
                view?.hideRefreshProgress()
            }
        } else {
            loadEpisodesFromNetwork(
                noCache = true,
                showProgress = {},
                hideProgress = { view?.hideRefreshProgress() })
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
        val showSpecials = preferences.showSpecials
        val seasonsList = episodesRepository.allSeasons(showId)
            .filter { season -> showSpecials || season.number > 0 }
            .map(SeasonViewModel.Companion::map)
        val oldSeasonsViewModel = seasonsViewModel
        seasonsViewModel = SeasonsViewModel(seasonsList)
        oldSeasonsViewModel.asList().forEach { season ->
            seasonsViewModel[season.number]?.isExpanded = season.isExpanded
        }
        view?.displayEpisodes(seasonsList)
        view?.hideEpisodesProgress()
    }

    private fun loadEpisodesFromNetwork(
        noCache: Boolean = false,
        showProgress: () -> Unit = { view?.showEpisodesProgress() },
        hideProgress: () -> Unit = { view?.hideEpisodesProgress() })
    {
        val seasonNumbers = showDetails?.seasonNumbers
        if (seasonNumbers == null) {
            loge { "Can't load episodes list without ShowDetails" }
            return
        }

        showProgress()
        view?.hideEpisodesError()

        launch {
            try {
                val showSpecials = preferences.showSpecials
                showSeasons = seasonNumbers
                    .filter { season -> showSpecials || season > 0 }
                    .mapNotNull { seasonNumber ->
                        log { "map season #$seasonNumber" }
                        showRepository.season(showTmdbId = showId, seasonNumber = seasonNumber, noCache = noCache)
                    }
                val seasonsList = showSeasons!!.map(SeasonViewModel.Companion::map)
                seasonsViewModel = SeasonsViewModel(seasonsList)
                view?.displayEpisodes(seasonsList)
            } catch (error: Throwable) {
                loge { "loadEpisodesFromNetwork: $error" }
                view?.showEpisodesError()
            } finally {
                hideProgress()
            }
        }
    }

    private fun processEpisodeWatchedStateToggle(episode: EpisodeViewModel) {
        val isWatched = !episode.isWatched

        val firstNotWatchedNumber = episodesRepository.firstNotWatchedEpisode(showId)
        if (isWatched && firstNotWatchedNumber != null && firstNotWatchedNumber < episode.number) {
            view?.showCheckAllPreviousEpisodesPrompt(
                onCheckAllPrevious = {
                    markAllEpisodesWatchedUpTo(episode.number)
                },
                onCheckOnlyThis = {
                    setEpisodeWatched(episode, isWatched)
                },
                onCancel = {
                    view?.reloadSeason(episode.number.season)
                }
            )
        } else {
            setEpisodeWatched(episode, isWatched)
        }
    }

    private fun processSeasonWatchedStateToggled(season: SeasonViewModel) {
        val isWatched = !season.isWatched

        val firstNotWatchedSeason = episodesRepository.firstNotWatchedEpisode(showId)?.season ?: season.number
        if (isWatched && firstNotWatchedSeason < season.number) {
            view?.showCheckAllPreviousEpisodesPrompt(
                onCheckAllPrevious = {
                    markAllSeasonsWatchedUpTo(season.number)
                },
                onCheckOnlyThis = {
                    setSeasonWatched(season, isWatched)
                },
                onCancel = {
                    view?.reloadSeason(season.number)
                }
            )
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

    companion object {
        private fun buildShareText(name: String, year: Int?, imdbId: String?, homePageUrl: String?): String {
            return buildString {
                append(name)
                if (year != null) append(" ($year)")
                if (imdbId != null) {
                    val imdbUrl = imdbId.let { "https://www.imdb.com/title/$it" }
                    append("\n$imdbUrl")
                } else if (homePageUrl != null) {
                    append("\n$homePageUrl")
                }
            }
        }
    }
}
