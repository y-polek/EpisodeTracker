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
    fun displayAddToMyShowsConfirmation(showName: String, callback: (confirmed: Boolean) -> Unit)
    fun showCheckAllPreviousEpisodesPrompt(onCheckAllPrevious: () -> Unit, onCheckOnlyThis: () -> Unit, onCancel: () -> Unit)
    fun displayOptionsMenu(isInMyShows: Boolean)
    fun shareText(text: String)

    fun displayShowDetails(show: ShowDetailsViewModel)
    fun displayTrailers(trailers: List<TrailerViewModel>)
    fun displayCast(castMembers: List<CastMemberViewModel>)
    fun displayRecommendations(recommendations: List<RecommendationViewModel>)
    fun displayImdbRating(rating: Float)
    fun openRecommendation(show: RecommendationViewModel)

    fun displayEpisodes(seasons: List<SeasonViewModel>)
    fun showEpisodesProgress()
    fun hideEpisodesProgress()
    fun showEpisodesError()
    fun hideEpisodesError()
    fun reloadSeason(season: Int)
    fun reloadAllSeasons()
}
