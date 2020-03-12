package dev.polek.episodetracker.common.presentation.showdetails.presenter

import dev.polek.episodetracker.common.datasource.themoviedb.TmdbService.Companion.backdropImageUrl
import dev.polek.episodetracker.common.datasource.themoviedb.TmdbService.Companion.profileImageUrl
import dev.polek.episodetracker.common.presentation.BasePresenter
import dev.polek.episodetracker.common.presentation.showdetails.model.CastMemberViewModel
import dev.polek.episodetracker.common.presentation.showdetails.model.RecommendationViewModel
import dev.polek.episodetracker.common.presentation.showdetails.model.ShowDetailsViewModel
import dev.polek.episodetracker.common.presentation.showdetails.model.TrailerViewModel
import dev.polek.episodetracker.common.presentation.showdetails.view.AboutShowView
import dev.polek.episodetracker.common.repositories.MyShowsRepository
import dev.polek.episodetracker.common.repositories.ShowRepository
import kotlinx.coroutines.launch

class AboutShowPresenter(
    private val showId: Int,
    private val myShowsRepository: MyShowsRepository,
    private val showRepository: ShowRepository) : BasePresenter<AboutShowView>()
{
    override fun attachView(view: AboutShowView) {
        super.attachView(view)
        loadShow()

        launch {
            loadAdditionalInfo()
        }
    }

    fun onRecommendationClicked(recommendation: RecommendationViewModel) {
        view?.openRecommendation(recommendation)
    }

    private fun loadShow() {
        val show = myShowsRepository.showDetails(showId) ?: return

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

    private suspend fun loadAdditionalInfo() {
        val show = showRepository.showDetails(showId)

        if (!myShowsRepository.isInMyShows(showId)) {
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

        val trailers = show.videos.map { video ->
            TrailerViewModel(
                name = video.name.orEmpty(),
                youtubeKey = video.key.orEmpty())
        }
        val castMembers = show.cast.map { member ->
            CastMemberViewModel(
                name = member.name.orEmpty(),
                character = member.character.orEmpty(),
                portraitImageUrl = member.profilePath?.let(::profileImageUrl))
        }
        val recommendations = show.recommendations.map { recommendation ->
            val year = recommendation.year
            val network = recommendation.network?.name
            val subhead = when {
                year != null && network != null -> "$year ∙ $network"
                year != null -> "$year"
                network != null -> network
                else -> ""
            }
            RecommendationViewModel(
                showId = recommendation.tmdbId ?: 0,
                name = recommendation.name.orEmpty(),
                imageUrl = recommendation.backdropPath?.let(::backdropImageUrl),
                subhead = subhead)
        }

        view?.displayTrailers(trailers)
        view?.displayCast(castMembers)
        view?.displayRecommendations(recommendations)

        val imdbId = show.externalIds?.imdbId
        if (imdbId != null) {
            val imdbRating = showRepository.imdbRating(imdbId)
            if (imdbRating != null) {
                view?.displayImdbRating(imdbRating)
            }
        }
    }
}
