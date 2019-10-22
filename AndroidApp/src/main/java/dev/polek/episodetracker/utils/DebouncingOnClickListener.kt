package dev.polek.episodetracker.utils

import android.view.View

/**
 * A [View.OnClickListener] that debounces multiple clicks posted in the same frame.
 * A click on one button disables all buttons for that frame.
 */
abstract class DebouncingOnClickListener : View.OnClickListener {

    override fun onClick(view: View) {
        if (enabled) {
            enabled = false
            view.post(ENABLE_AGAIN)
            doClick(view)
        }
    }

    abstract fun doClick(view: View)

    companion object {
        private var enabled = true
        private val ENABLE_AGAIN = Runnable { enabled = true }
    }
}

inline fun View.doOnClick(crossinline listener: (view: View) -> Unit) {
    this.setOnClickListener(object : DebouncingOnClickListener() {
        override fun doClick(view: View) {
            listener(view)
        }
    })
}
