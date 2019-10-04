package dev.polek.episodetracker.myshows.model

sealed class MyShowsListItem {
    sealed class  GroupViewModel(
        val name: String,
        val expanded: Boolean) : MyShowsListItem()
    {
        class UpcomingGroupViewModel(name: String, expanded: Boolean) : GroupViewModel(name, expanded)
        class ToBeAnnouncedGroupViewModel(name: String, expanded: Boolean) : GroupViewModel(name, expanded)
        class EndedGroupViewModel(name: String, expanded: Boolean) : GroupViewModel(name, expanded)
    }

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
