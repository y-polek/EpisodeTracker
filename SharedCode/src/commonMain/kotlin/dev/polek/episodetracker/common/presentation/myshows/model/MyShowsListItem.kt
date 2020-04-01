package dev.polek.episodetracker.common.presentation.myshows.model

sealed class MyShowsListItem {
    sealed class  GroupViewModel(
        val name: String,
        val expanded: Boolean) : MyShowsListItem()
    {
        class UpcomingGroupViewModel(name: String, expanded: Boolean) : GroupViewModel(name, expanded)
        class ToBeAnnouncedGroupViewModel(name: String, expanded: Boolean) : GroupViewModel(name, expanded)
        class EndedGroupViewModel(name: String, expanded: Boolean) : GroupViewModel(name, expanded)
        class ArchivedGroupViewModel(name: String, expanded: Boolean) : GroupViewModel(name, expanded)
    }

    open class ShowViewModel(
        val id: Int,
        val name: String,
        val backdropUrl: String?): MyShowsListItem()
    {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as ShowViewModel

            if (id != other.id) return false
            if (name != other.name) return false
            if (backdropUrl != other.backdropUrl) return false

            return true
        }

        override fun hashCode(): Int {
            var result = id
            result = 31 * result + name.hashCode()
            result = 31 * result + (backdropUrl?.hashCode() ?: 0)
            return result
        }

    }

    class UpcomingShowViewModel(
        id: Int,
        name: String,
        backdropUrl: String?,
        val episodeNumber: String,
        val episodeName: String,
        val timeLeft: String): ShowViewModel(id, name, backdropUrl)
    {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false
            if (!super.equals(other)) return false

            other as UpcomingShowViewModel

            if (episodeNumber != other.episodeNumber) return false
            if (episodeName != other.episodeName) return false
            if (timeLeft != other.timeLeft) return false

            return true
        }

        override fun hashCode(): Int {
            var result = super.hashCode()
            result = 31 * result + episodeNumber.hashCode()
            result = 31 * result + episodeName.hashCode()
            result = 31 * result + timeLeft.hashCode()
            return result
        }

    }
}
