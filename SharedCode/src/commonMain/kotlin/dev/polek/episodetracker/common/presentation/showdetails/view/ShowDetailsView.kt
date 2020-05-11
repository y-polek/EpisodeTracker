package dev.polek.episodetracker.common.presentation.showdetails.view

import dev.polek.episodetracker.common.presentation.showdetails.model.*

interface ShowDetailsView {

    fun displayShowHeader(show: ShowHeaderViewModel)
    fun showProgress()
    fun hideProgress()
    fun showError()
    fun hideError()
    fun displayAddToMyShowsButton()
    fun displayAddToMyShowsProgress()
    fun hideAddToMyShowsButton()
    fun displayArchivedBadge()
    fun hideArchivedBadge()
    fun displayAddToMyShowsConfirmation(showName: String, callback: (confirmed: Boolean) -> Unit)
    fun showCheckAllPreviousEpisodesPrompt(onCheckAllPrevious: () -> Unit, onCheckOnlyThis: () -> Unit, onCancel: () -> Unit)
    fun displayOptionsMenu(isInMyShows: Boolean, isArchived: Boolean)
    fun shareText(text: String)
    fun displayContentRatingInfo(rating: String, text: String)

    fun displayShowDetails(show: ShowDetailsViewModel)
    fun displayTrailers(trailers: List<TrailerViewModel>)
    fun displayCast(castMembers: List<CastMemberViewModel>)
    fun displayRecommendations(recommendations: List<RecommendationViewModel>)
    fun updateRecommendation(show: RecommendationViewModel)
    fun displayRemoveRecommendationConfirmation(show: RecommendationViewModel, callback: (confirmed: Boolean) -> Unit)
    fun openRecommendation(show: RecommendationViewModel)
    fun displayImdbRating(rating: Float)

    fun displayEpisodes(seasons: List<SeasonViewModel>)
    fun showEpisodesProgress()
    fun hideEpisodesProgress()
    fun showEpisodesError()
    fun hideEpisodesError()
    fun reloadSeason(number: Int)
    fun hideRefreshProgress()
}
