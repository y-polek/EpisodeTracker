package dev.polek.episodetracker.towatch

import android.text.Html
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.swipe.SwipeLayout
import dev.polek.episodetracker.R
import dev.polek.episodetracker.common.presentation.towatch.ToWatchShowViewModel
import dev.polek.episodetracker.databinding.ToWatchShowLayoutBinding
import dev.polek.episodetracker.utils.doOnClick
import dev.polek.episodetracker.utils.layoutInflater
import dev.polek.episodetracker.utils.loadImage
import dev.polek.episodetracker.utils.recyclerview.AbstractSwipeListener
import dev.polek.episodetracker.utils.recyclerview.CloseSwipeActionsScrollListener

class ToWatchAdapter : RecyclerView.Adapter<ToWatchAdapter.ViewHolder>(), CloseSwipeActionsScrollListener.SwipeActionsClosable {

    var listener: Listener? = null

    var shows: List<ToWatchShowViewModel> = emptyList()
        set(value) {
            val diff = DiffUtil.calculateDiff(DiffUtilCallback(oldList = shows, newList = value))
            field = value
            diff.dispatchUpdatesTo(this)
        }

    override fun getItemCount() = shows.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ToWatchShowLayoutBinding.inflate(parent.layoutInflater, parent, false)
        return ViewHolder(
            binding,
            onShowClicked = { position ->
                listener?.onShowClicked(shows[position])
            },
            onCheckButtonClicked = { position ->
                listener?.onCheckButtonClicked(shows[position])
            },
            onArchiveButtonClicked = { position ->
                listener?.onArchiveButtonClicked(shows[position])
            },
            onMarkAllWatchedButtonClicked = { position ->
                listener?.onMarkAllWatchedButtonClicked(shows[position])
            },
            onActionStartOpen = { position ->
                for (i in 0 until itemCount) {
                    if (i != position) {
                        notifyItemChanged(i, Payload.CloseSwipeAction)
                    }
                }
            }
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val show = shows[position]
        val binding = holder.binding

        binding.title.text = show.name

        @Suppress("DEPRECATION")
        binding.subtitle.text = Html.fromHtml("<b>${show.nextEpisodeNumberText}</b> | ${show.nextEpisodeName}")

        binding.episodesCount.text = show.episodeCount.toString()
        binding.episodesLabel.text = holder.itemView.resources.getQuantityString(R.plurals.episodes, show.episodeCount)

        binding.specialsBadge.isVisible = show.isSpecials

        binding.image.loadImage(show.imageUrl)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) return super.onBindViewHolder(holder, position, payloads)

        payloads.forEach { payload ->
            when (payload) {
                is Payload.CloseSwipeAction -> {
                    holder.binding.swipeLayout.close()
                }
                else -> throw NotImplementedError("Unknown payload: $payload")
            }
        }
    }

    override fun closeSwipeActions() {
        notifyItemRangeChanged(0, itemCount, Payload.CloseSwipeAction)
    }

    class ViewHolder(
        val binding: ToWatchShowLayoutBinding,
        onShowClicked: (position: Int) -> Unit,
        onCheckButtonClicked: (position: Int) -> Unit,
        onArchiveButtonClicked: (position: Int) -> Unit,
        onMarkAllWatchedButtonClicked: (position: Int) -> Unit,
        onActionStartOpen: (position: Int) -> Unit) : RecyclerView.ViewHolder(binding.root)
    {
        init {
            binding.swipeLayout.addSwipeListener(object : AbstractSwipeListener() {
                override fun onStartOpen(layout: SwipeLayout) {
                    val position = bindingAdapterPosition
                    if (position == RecyclerView.NO_POSITION) return
                    onActionStartOpen(position)
                }
            })

            binding.cardView.doOnClick {
                val position = bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) return@doOnClick
                onShowClicked(position)
            }

            binding.checkButton.doOnClick {
                val position = bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) return@doOnClick
                onCheckButtonClicked(position)
            }

            binding.archiveButton.doOnClick {
                val position = bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) return@doOnClick
                onArchiveButtonClicked(position)
            }

            binding.markAllWatchedButton.doOnClick {
                val position = bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) return@doOnClick
                onMarkAllWatchedButtonClicked(position)
            }
        }
    }

    interface Listener {
        fun onShowClicked(show: ToWatchShowViewModel)
        fun onCheckButtonClicked(show: ToWatchShowViewModel)
        fun onArchiveButtonClicked(show: ToWatchShowViewModel)
        fun onMarkAllWatchedButtonClicked(show: ToWatchShowViewModel)
    }

    private class DiffUtilCallback(
        val oldList: List<ToWatchShowViewModel>,
        val newList: List<ToWatchShowViewModel>) : DiffUtil.Callback()
    {
        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

    private sealed class Payload {
        object CloseSwipeAction
    }
}
