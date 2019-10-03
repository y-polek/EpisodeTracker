package dev.polek.episodetracker.myshows.model

sealed class MyShowsListItem {
    data class GroupViewModel(
        val name: String,
        val expanded: Boolean
    ) : MyShowsListItem()

    data class ShowViewModel(
        val name: String,
        val backdropUrl: String?
    ): MyShowsListItem()

    data class UpcomingShowViewModel(
        val name: String,
        val backdropUrl: String?,
        val episodeNumber: String,
        val episodeName: String,
        val timeLeft: String
    ): MyShowsListItem()

}
