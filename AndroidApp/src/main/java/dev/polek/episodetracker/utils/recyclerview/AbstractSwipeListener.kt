package dev.polek.episodetracker.utils.recyclerview

import androidx.recyclerview.widget.RecyclerView
import com.daimajia.swipe.SwipeLayout

abstract class AbstractSwipeListener : SwipeLayout.SwipeListener {

    override fun onOpen(layout: SwipeLayout) {}
    override fun onUpdate(layout: SwipeLayout, leftOffset: Int, topOffset: Int) {}
    override fun onStartOpen(layout: SwipeLayout) {}
    override fun onStartClose(layout: SwipeLayout) {}
    override fun onHandRelease(layout: SwipeLayout, xvel: Float, yvel: Float) {}
    override fun onClose(layout: SwipeLayout) {}
}
