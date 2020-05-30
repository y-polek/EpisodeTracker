package dev.polek.episodetracker.myshows

import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.polek.episodetracker.R
import dev.polek.episodetracker.common.presentation.myshows.model.MyShowsListItem
import dev.polek.episodetracker.databinding.GroupHeaderLayoutBinding
import dev.polek.episodetracker.databinding.MyShowLayoutBinding
import dev.polek.episodetracker.databinding.UpcomingShowLayoutBinding
import dev.polek.episodetracker.utils.layoutInflater
import dev.polek.episodetracker.utils.loadImage
import dev.polek.episodetracker.utils.recyclerview.CloseSwipeActionsScrollListener

class MyShowsAdapter(
    @StringRes val titleRes: Int,
    isExpanded: Boolean,
    val onShowClicked: (show: MyShowsListItem.ShowViewModel) -> Unit,
    val onRemoveButtonClicked: (show: MyShowsListItem.ShowViewModel) -> Unit,
    val onArchiveButtonClicked: (show: MyShowsListItem.ShowViewModel) -> Unit,
    val onUnarchiveButtonClicked: (show: MyShowsListItem.ShowViewModel) -> Unit,
    val onExpandStateChanged: (isExpanded: Boolean) -> Unit) : RecyclerView.Adapter<MyShowsViewHolder>(), CloseSwipeActionsScrollListener.SwipeActionsClosable
{
    var shows: List<MyShowsListItem.ShowViewModel> = emptyList()
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
        return when {
            position == 0 -> R.layout.group_header_layout
            showAtPosition(position) is MyShowsListItem.UpcomingShowViewModel -> R.layout.upcoming_show_layout
            else -> R.layout.my_show_layout
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
                MyShowsViewHolder.UpcomingShowViewHolder(binding,
                    onClicked = { position ->
                        onShowClicked(showAtPosition(position))
                    },
                    onRemoveButtonClicked = { position ->
                        onRemoveButtonClicked(showAtPosition(position))
                    },
                    onArchiveButtonClicked = { position ->
                        onArchiveButtonClicked(showAtPosition(position))
                    },
                    onActionStartOpen = { position ->
                        for (i in 0 until itemCount) {
                            if (i != position) {
                                notifyItemChanged(i, Payload.CloseSwipeAction)
                            }
                        }
                    })
            }
            R.layout.my_show_layout -> {
                val binding = MyShowLayoutBinding.inflate(parent.layoutInflater, parent, false)
                return MyShowsViewHolder.ShowViewHolder(binding,
                    onClicked = { position ->
                        onShowClicked(showAtPosition(position))
                    },
                    onRemoveButtonClicked = { position ->
                        onRemoveButtonClicked(showAtPosition(position))
                    },
                    onArchiveButtonClicked = { position ->
                        onArchiveButtonClicked(showAtPosition(position))
                    },
                    onUnarchiveButtonClicked = { position ->
                        onUnarchiveButtonClicked(showAtPosition(position))
                    },
                    onActionStartOpen = { position ->
                        for (i in 0 until itemCount) {
                            if (i != position) {
                                notifyItemChanged(i, Payload.CloseSwipeAction)
                            }
                        }
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
                val show = showAtPosition(position) as MyShowsListItem.UpcomingShowViewModel
                holder.binding.name.text = show.name
                holder.binding.episodeName.text = show.episodeName
                holder.binding.episodeNumber.text = show.episodeNumber
                holder.binding.timeLeft.text = show.timeLeft
                holder.binding.image.loadImage(show.backdropUrl)
            }
            is MyShowsViewHolder.ShowViewHolder -> {
                val show = showAtPosition(position)
                holder.binding.name.text = show.name
                holder.binding.image.loadImage(show.backdropUrl)
                holder.binding.archiveButton.isVisible = !show.isArchived
                holder.binding.unarchiveButton.isVisible = show.isArchived
            }
        }
    }

    override fun onBindViewHolder(holder: MyShowsViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) return super.onBindViewHolder(holder, position, payloads)

        payloads.forEach { payload ->
            when (payload) {
                is Payload.CloseSwipeAction -> {
                    holder.closeSwipeActions()
                }
                else -> throw NotImplementedError("Unknown payload: $payload")
            }
        }
    }

    override fun closeSwipeActions() {
        notifyItemRangeChanged(0, itemCount, Payload.CloseSwipeAction)
    }

    private fun showAtPosition(position: Int): MyShowsListItem.ShowViewModel {
        return shows[position - 1]
    }

    sealed class Payload {
        object CloseSwipeAction
    }
}
