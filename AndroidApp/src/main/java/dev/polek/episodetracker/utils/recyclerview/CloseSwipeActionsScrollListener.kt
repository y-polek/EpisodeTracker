package dev.polek.episodetracker.utils.recyclerview

import androidx.recyclerview.widget.MergeAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.polek.episodetracker.common.logging.logw

object CloseSwipeActionsScrollListener : RecyclerView.OnScrollListener() {

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
            recyclerView.adapter?.let(::closeActions)
        }
    }

    private fun closeActions(adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>) {
        when (adapter) {
            is SwipeActionsClosable -> {
                adapter.closeSwipeActions()
            }
            is MergeAdapter -> {
                adapter.adapters.forEach(::closeActions)
            }
            else -> {
                logw { "Adapter must implement SwipeActionsClosable interface to work with CloseSwipeActionsScrollListener: $adapter" }
            }
        }
    }

    interface SwipeActionsClosable {
        fun closeSwipeActions()
    }
}
