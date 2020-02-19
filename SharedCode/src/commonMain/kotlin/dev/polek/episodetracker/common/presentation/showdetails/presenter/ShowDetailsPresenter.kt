package dev.polek.episodetracker.common.presentation.showdetails.presenter

import dev.polek.episodetracker.common.logging.log
import dev.polek.episodetracker.common.presentation.BasePresenter
import dev.polek.episodetracker.common.presentation.showdetails.model.ShowHeaderViewModel
import dev.polek.episodetracker.common.presentation.showdetails.view.ShowDetailsView
import dev.polek.episodetracker.common.repositories.MyShowsRepository

class ShowDetailsPresenter(
    private val showId: Int,
    private val repository: MyShowsRepository) : BasePresenter<ShowDetailsView>()
{

    override fun attachView(view: ShowDetailsView) {
        super.attachView(view)
        loadShow()
    }

    private fun loadShow() {
        val show = repository.showDetails(showId)
        if (show == null) {
            view?.close()
            log("Can't find show with $showId ID in My Shows")
            return
        }


        val year = show.year
        val endYear = if (show.isEnded) show.lastYear else null
        val yearsText = when {
            year != null && endYear != null -> "$year - $endYear"
            year != null -> "$year"
            else -> null
        }
        val network = show.networkName

        val subhead = when {
            yearsText != null && network != null -> "$yearsText ∙ $network"
            yearsText != null -> "$yearsText - $endYear"
            else -> ""
        }

        val headerViewModel = ShowHeaderViewModel(
            name = show.name,
            imageUrl = show.imageUrl,
            subhead = subhead,
            rating = show.contentRating.orEmpty())

        view?.displayShowHeader(headerViewModel)
    }
}
