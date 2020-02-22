package dev.polek.episodetracker.common.presentation.showdetails.presenter

import dev.polek.episodetracker.common.datasource.themoviedb.TmdbService.Companion.backdropImageUrl
import dev.polek.episodetracker.common.logging.log
import dev.polek.episodetracker.common.presentation.BasePresenter
import dev.polek.episodetracker.common.presentation.showdetails.model.ShowHeaderViewModel
import dev.polek.episodetracker.common.presentation.showdetails.view.ShowDetailsView
import dev.polek.episodetracker.common.repositories.MyShowsRepository
import dev.polek.episodetracker.common.repositories.ShowRepository
import kotlinx.coroutines.launch

class ShowDetailsPresenter(
    private val showId: Int,
    private val myShowsRepository: MyShowsRepository,
    private val showRepository: ShowRepository) : BasePresenter<ShowDetailsView>()
{
    override fun attachView(view: ShowDetailsView) {
        super.attachView(view)

        launch {
            if (myShowsRepository.isInMyShows(showId)) {
                loadFromMyShows()
            } else {
                loadFromRemote()
            }
        }
    }

    private fun loadFromMyShows() {
        val show = myShowsRepository.showDetails(showId)
        if (show == null) {
            view?.close()
            log("Can't find show with $showId ID in My Shows")
            return
        }

        val headerViewModel = ShowHeaderViewModel(
            name = show.name,
            imageUrl = show.imageUrl,
            rating = show.contentRating.orEmpty(),
            year = show.year,
            endYear = if (show.isEnded) show.lastYear else null,
            network = show.networkName)

        view?.displayShowHeader(headerViewModel)
    }

    private suspend fun loadFromRemote() {
        val show = showRepository.showDetails(showId)

        val headerViewModel = ShowHeaderViewModel(
            name = show.name.orEmpty(),
            imageUrl = show.backdropPath?.let(::backdropImageUrl),
            rating = show.contentRating.orEmpty(),
            year = show.year,
            endYear = if (show.isEnded) show.lastYear else null,
            network = show.network?.name)

        view?.displayShowHeader(headerViewModel)
    }
}
