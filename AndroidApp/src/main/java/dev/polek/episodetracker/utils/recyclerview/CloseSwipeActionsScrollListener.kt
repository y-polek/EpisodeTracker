package dev.polek.episodetracker.utils.recyclerview

import androidx.recyclerview.widget.RecyclerView
import dev.polek.episodetracker.common.logging.logw

object CloseSwipeActionsScrollListener : RecyclerView.OnScrollListener() {

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
            closeActions(recyclerView)
        }
    }

    private fun closeActions(recyclerView: RecyclerView) {
        val adapter = recyclerView.adapter ?: return
        if (adapter is SwipeActionsClosable) {
            adapter.closeSwipeActions()
        } else {
            logw { "Adapter must implement SwipeActionsClosable interface to work with CloseSwipeActionsScrollListener" }
        }
    }

    interface SwipeActionsClosable {
        fun closeSwipeActions()
    }
}
