package dev.polek.episodetracker.utils.swipeactions

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension
import dev.polek.episodetracker.common.logging.log

class ItemTouchHelperCallback(private val direction: Int) : ItemTouchHelperExtension.Callback() {

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return makeMovementFlags(0, direction)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        log { "" }
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder): Boolean
    {
        return false
    }

    override fun onChildDraw(
        canvas: Canvas,
        recyclerView: RecyclerView?,
        viewHolder: RecyclerView.ViewHolder?,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean)
    {
        (viewHolder as? SwipeActionExtension)?.getContentView()?.translationX = dX
    }

    companion object {
        fun left(): ItemTouchHelperCallback = ItemTouchHelperCallback(ItemTouchHelper.LEFT)
        fun leftAndRight(): ItemTouchHelperCallback = ItemTouchHelperCallback(ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
    }
}
