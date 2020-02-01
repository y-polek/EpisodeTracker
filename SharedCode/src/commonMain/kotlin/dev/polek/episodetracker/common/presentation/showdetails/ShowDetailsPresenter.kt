package dev.polek.episodetracker.common.presentation.showdetails

import dev.polek.episodetracker.common.logging.log
import dev.polek.episodetracker.common.presentation.BasePresenter
import dev.polek.episodetracker.common.presentation.showdetails.model.ShowDetailsViewModel
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

        val startYear = show.year?.toString().orEmpty()
        val endYear = if (show.isEnded) show.lastYear?.toString().orEmpty() else "Present"

        val showViewModel = ShowDetailsViewModel(
            name = show.name,
            imageUrl = show.imageUrl,
            years = "$startYear - $endYear",
            contentRating = show.contentRating.orEmpty()
        )


        view?.displayShowDetails(showViewModel)
    }
}
