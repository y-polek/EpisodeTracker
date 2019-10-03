package dev.polek.episodetracker.myshows

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView

class ScrollingBehavior(context: Context, attrs: AttributeSet)
    : CoordinatorLayout.Behavior<RecyclerView>(context, attrs)
{
    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: RecyclerView,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int): Boolean
    {
        return axes and ViewCompat.SCROLL_AXIS_VERTICAL != 0
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: RecyclerView,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int)
    {
        child.scrollBy(dx, dy)
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
    }
}
