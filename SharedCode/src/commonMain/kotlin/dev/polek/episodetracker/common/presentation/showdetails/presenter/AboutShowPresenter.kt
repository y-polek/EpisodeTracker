package dev.polek.episodetracker.common.presentation.showdetails.presenter

import dev.polek.episodetracker.common.presentation.BasePresenter
import dev.polek.episodetracker.common.presentation.showdetails.model.ShowDetailsViewModel
import dev.polek.episodetracker.common.presentation.showdetails.view.AboutShowView
import dev.polek.episodetracker.common.repositories.MyShowsRepository

class AboutShowPresenter(
    private val showId: Int,
    private val repository: MyShowsRepository) : BasePresenter<AboutShowView>()
{
    override fun attachView(view: AboutShowView) {
        super.attachView(view)
        loadShow()
    }

    private fun loadShow() {
        val show = repository.showDetails(showId) ?: return

        val detailsViewModel = ShowDetailsViewModel(
            overview = show.overview,
            contentRating = show.contentRating.orEmpty(),
            genres = show.genres,

            // TODO: show read data
            airTime = "Thu 20:30",
            duration = "60 min")

        view?.displayShowDetails(detailsViewModel)
    }
}
