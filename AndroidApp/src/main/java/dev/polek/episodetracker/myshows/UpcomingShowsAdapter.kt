package dev.polek.episodetracker.myshows

import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.polek.episodetracker.R
import dev.polek.episodetracker.common.presentation.myshows.model.MyShowsListItem
import dev.polek.episodetracker.databinding.GroupHeaderLayoutBinding
import dev.polek.episodetracker.databinding.UpcomingShowLayoutBinding
import dev.polek.episodetracker.utils.layoutInflater
import dev.polek.episodetracker.utils.loadImage

class UpcomingShowsAdapter(
    @StringRes val titleRes: Int,
    isExpanded: Boolean,
    val onShowClicked: (show: MyShowsListItem.ShowViewModel) -> Unit,
    val onExpandStateChanged: (isExpanded: Boolean) -> Unit) : RecyclerView.Adapter<MyShowsViewHolder>()
{
    var shows: List<MyShowsListItem.UpcomingShowViewModel> = emptyList()
        set(value) {
            val diff = DiffUtil.calculateDiff(MyShowsDiffUtilCallback(oldShows = field, newShows = value, isExpanded = isExpanded))
            field = value
            diff.dispatchUpdatesTo(this)
        }

    private var isExpanded: Boolean = isExpanded
        set(value) {
            field = value
            notifyItemChanged(0)
            if (isExpanded) {
                notifyItemRangeInserted(1, shows.size)
            } else {
                notifyItemRangeRemoved(1, shows.size)
            }
        }

    override fun getItemCount(): Int {
        return when {
            shows.isEmpty() -> 0
            isExpanded -> 1/*header*/ + shows.size
            else -> 1/*header*/
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> R.layout.group_header_layout
            else -> R.layout.upcoming_show_layout
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyShowsViewHolder {
        return when (viewType) {
            R.layout.group_header_layout -> {
                val binding = GroupHeaderLayoutBinding.inflate(parent.layoutInflater, parent, false)
                MyShowsViewHolder.GroupHeaderViewHolder(binding, onClicked = {
                    isExpanded = !isExpanded
                    onExpandStateChanged(isExpanded)
                })
            }
            R.layout.upcoming_show_layout -> {
                val binding = UpcomingShowLayoutBinding.inflate(parent.layoutInflater, parent, false)
                MyShowsViewHolder.UpcomingShowViewHolder(binding, onClicked = { position ->
                    onShowClicked(showAtPosition(position))
                })
            }
            else -> throw IllegalStateException("Unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: MyShowsViewHolder, position: Int) {
        when (holder) {
            is MyShowsViewHolder.GroupHeaderViewHolder -> {
                holder.binding.name.setText(titleRes)
                val arrowIcon = if (isExpanded) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down
                holder.binding.name.setCompoundDrawablesWithIntrinsicBounds(0, 0, arrowIcon, 0)
            }
            is MyShowsViewHolder.UpcomingShowViewHolder -> {
                val show = showAtPosition(position)
                holder.binding.name.text = show.name
                holder.binding.episodeName.text = show.episodeName
                holder.binding.episodeNumber.text = show.episodeNumber
                holder.binding.timeLeft.text = show.timeLeft
                holder.binding.image.loadImage(show.backdropUrl)
            }
        }
    }

    private fun showAtPosition(position: Int): MyShowsListItem.UpcomingShowViewModel {
        return shows[position - 1]
    }
}
