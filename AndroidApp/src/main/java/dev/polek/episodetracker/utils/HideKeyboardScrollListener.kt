package dev.polek.episodetracker.utils

import androidx.recyclerview.widget.RecyclerView

class HideKeyboardScrollListener : RecyclerView.OnScrollListener() {

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
            recyclerView.context.hideKeyboard(recyclerView)
        }
    }
}
