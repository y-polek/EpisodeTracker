package dev.polek.episodetracker.common.presentation.showdetails.presenter

import dev.polek.episodetracker.common.presentation.BasePresenter
import dev.polek.episodetracker.common.presentation.showdetails.model.ShowDetailsViewModel
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
            loadTrailers()
        }
    }

    private fun loadShow() {
        val show = myShowsRepository.showDetails(showId) ?: return

        val detailsViewModel = ShowDetailsViewModel(
            overview = show.overview,
            contentRating = show.contentRating.orEmpty(),
            genres = show.genres,

            // TODO: show read data
            airTime = "Thu 20:30",
            duration = "60 min")

        view?.displayShowDetails(detailsViewModel)
    }

    private suspend fun loadTrailers() {
        val trailers = showRepository.trailers(showId)
        view?.displayTrailers(trailers)
    }
}
