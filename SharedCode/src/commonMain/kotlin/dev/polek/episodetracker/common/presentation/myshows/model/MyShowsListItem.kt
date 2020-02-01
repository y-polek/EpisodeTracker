package dev.polek.episodetracker.common.presentation.myshows.model

sealed class MyShowsListItem {
    sealed class  GroupViewModel(
        val name: String,
        val expanded: Boolean) : MyShowsListItem()
    {
        class UpcomingGroupViewModel(name: String, expanded: Boolean) : GroupViewModel(name, expanded)
        class ToBeAnnouncedGroupViewModel(name: String, expanded: Boolean) : GroupViewModel(name, expanded)
        class EndedGroupViewModel(name: String, expanded: Boolean) : GroupViewModel(name, expanded)
    }

    open class ShowViewModel(
        val id: Int,
        val name: String,
        val backdropUrl: String?
    ): MyShowsListItem()

    class UpcomingShowViewModel(
        id: Int,
        name: String,
        backdropUrl: String?,
        val episodeNumber: String,
        val episodeName: String,
        val timeLeft: String
    ): ShowViewModel(id, name, backdropUrl)
}
