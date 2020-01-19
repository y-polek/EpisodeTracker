package dev.polek.episodetracker.common.presentation.towatch

import dev.polek.episodetracker.common.presentation.BasePresenter

class ToWatchPresenter : BasePresenter<ToWatchView>() {

    override fun attachView(view: ToWatchView) {
        super.attachView(view)

        loadShows()
    }

    private fun loadShows() {

        val shows = listOf<ToWatchShowViewModel>(
            ToWatchShowViewModel(
                name = "The Mandalorian",
                backdropUrl = "https://image.tmdb.org/t/p/w533_and_h300_bestv2/o7qi2v4uWQ8bZ1tW3KI0Ztn2epk.jpg",
                episodeNumber = "S01 E01",
                episodeName = "Chapter 1: The Mandalorian",
                episodeCount = 8
            )
        )
        view?.displayShows(shows)
    }
}
