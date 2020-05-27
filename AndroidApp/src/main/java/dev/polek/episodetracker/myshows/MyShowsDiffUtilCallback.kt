package dev.polek.episodetracker.myshows

import androidx.recyclerview.widget.DiffUtil
import dev.polek.episodetracker.common.presentation.myshows.model.MyShowsListItem

class MyShowsDiffUtilCallback(
    private val oldShows: List<MyShowsListItem.ShowViewModel>,
    private val newShows: List<MyShowsListItem.ShowViewModel>,
    private val isExpanded: Boolean) : DiffUtil.Callback()
{
    override fun getOldListSize(): Int {
        return when {
            oldShows.isEmpty() -> 0
            isExpanded -> 1 + oldShows.size
            else -> 1
        }
    }

    override fun getNewListSize(): Int {
        return when {
            newShows.isEmpty() -> 0
            isExpanded -> 1 + newShows.size
            else -> 1
        }
    }

    override fun areItemsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        if (oldPosition == 0) return newPosition == 0
        if (newPosition == 0) return oldPosition == 0
        return oldShows[oldPosition - 1].id == newShows[newPosition - 1].id
    }

    override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        if (oldPosition == 0) return newPosition == 0
        if (newPosition == 0) return oldPosition == 0
        return oldShows[oldPosition - 1] == newShows[newPosition - 1]
    }
}
