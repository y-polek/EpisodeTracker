package dev.polek.episodetracker.discover

import dev.polek.episodetracker.common.presentation.BasePresenter
import kotlinx.coroutines.launch

class DiscoverPresenter(private val repository: DiscoverRepository) : BasePresenter<DiscoverView>() {

    override fun attachView(view: DiscoverView) {
        super.attachView(view)

        launch {
            repository.search("star trek")
        }
    }
}
