package dev.polek.episodetracker.common.presentation.showdetails

import dev.polek.episodetracker.common.presentation.BasePresenter
import dev.polek.episodetracker.common.repositories.MyShowsRepository

class ShowDetailsPresenter(private val repository: MyShowsRepository) : BasePresenter<ShowDetailsView>() {

    override fun attachView(view: ShowDetailsView) {
        super.attachView(view)
    }
}
