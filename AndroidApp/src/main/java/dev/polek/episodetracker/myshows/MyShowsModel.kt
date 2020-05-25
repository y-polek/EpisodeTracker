package dev.polek.episodetracker.myshows

import androidx.annotation.IdRes
import dev.polek.episodetracker.R
import dev.polek.episodetracker.common.presentation.myshows.model.MyShowsListItem

class MyShowsModel(private val onModelModified: () -> Unit) {

    var lastWeekShows: List<MyShowsListItem.UpcomingShowViewModel> = emptyList()
        set(value) {
            field = value
            onModelModified()
        }

    var upcomingShows: List<MyShowsListItem.UpcomingShowViewModel> = emptyList()
        set(value) {
            field = value
            onModelModified()
        }

    var tbaShows: List<MyShowsListItem.ShowViewModel> = emptyList()
        set(value) {
            field = value
            onModelModified()
        }

    var endedShows: List<MyShowsListItem.ShowViewModel> = emptyList()
        set(value) {
            field = value
            onModelModified()
        }

    var archivedShow: List<MyShowsListItem.ShowViewModel> = emptyList()
        set(value) {
            field = value
            onModelModified()
        }

    fun count(): Int {
        return lastWeekShows.size + upcomingShows.size + tbaShows.size + endedShows.size + archivedShow.size
    }

    @IdRes
    fun viewType(position: Int): Int {
        return when (itemAt(position)) {
            is MyShowsListItem.UpcomingShowViewModel -> R.id.view_type_upcoming_show
            is MyShowsListItem.ShowViewModel -> R.id.view_type_show
        }
    }

    fun itemAt(position: Int): MyShowsListItem {
        return when {
            position < lastWeekShows.size -> {
                lastWeekShows[position]
            }
            position < lastWeekShows.size + upcomingShows.size -> {
                upcomingShows[position - lastWeekShows.size]
            }
            position < lastWeekShows.size + upcomingShows.size + tbaShows.size -> {
                tbaShows[position - lastWeekShows.size - upcomingShows.size]
            }
            position < lastWeekShows.size + upcomingShows.size + tbaShows.size + endedShows.size -> {
                endedShows[position - lastWeekShows.size - upcomingShows.size - tbaShows.size]
            }
            else -> {
                archivedShow[position - lastWeekShows.size - upcomingShows.size - tbaShows.size - endedShows.size]
            }
        }
    }
}
