package dev.polek.episodetracker.utils.recyclerview

import androidx.recyclerview.widget.RecyclerView
import dev.polek.episodetracker.utils.hideKeyboard

object HideKeyboardScrollListener : RecyclerView.OnScrollListener() {

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
            recyclerView.context.hideKeyboard(recyclerView)
        }
    }
}
