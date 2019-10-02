package dev.polek.episodetracker.myshows

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import dev.polek.episodetracker.utils.findChildOfType

class ScrollingBehavior(context: Context, attrs: AttributeSet)
    : CoordinatorLayout.Behavior<RecyclerView>(context, attrs)
{
    override fun onMeasureChild(
        parent: CoordinatorLayout,
        child: RecyclerView,
        parentWidthMeasureSpec: Int,
        widthUsed: Int,
        parentHeightMeasureSpec: Int,
        heightUsed: Int): Boolean
    {
        setRecyclerViewPaddingIfRequired(child, parent)

        return super.onMeasureChild(
            parent,
            child,
            parentWidthMeasureSpec,
            widthUsed,
            parentHeightMeasureSpec,
            heightUsed
        )
    }

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

    private fun setRecyclerViewPaddingIfRequired(recyclerView: RecyclerView, coordinatorLayout: CoordinatorLayout) {
        val appBarLayout = coordinatorLayout.findChildOfType(AppBarLayout::class.java) ?: return
        if (recyclerView.clipToPadding && appBarLayout.height > 0) {
            recyclerView.clipToPadding = false
            recyclerView.setPadding(0, appBarLayout.height, 0, 0)
            recyclerView.scrollToPosition(0)
        }
    }
}
