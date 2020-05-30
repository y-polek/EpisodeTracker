package dev.polek.episodetracker.myshows

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.swipe.SwipeLayout
import dev.polek.episodetracker.databinding.GroupHeaderLayoutBinding
import dev.polek.episodetracker.databinding.MyShowLayoutBinding
import dev.polek.episodetracker.databinding.UpcomingShowLayoutBinding
import dev.polek.episodetracker.utils.doOnClick
import dev.polek.episodetracker.utils.recyclerview.AbstractSwipeListener

sealed class MyShowsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun closeSwipeActions()

    class GroupHeaderViewHolder(
        val binding: GroupHeaderLayoutBinding,
        onClicked: (position: Int) -> Unit) : MyShowsViewHolder(binding.root)
    {
        init {
            itemView.doOnClick {
                val position = bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) return@doOnClick
                onClicked(position)
            }
        }

        override fun closeSwipeActions() {}
    }

    class UpcomingShowViewHolder(
        val binding: UpcomingShowLayoutBinding,
        onClicked: (position: Int) -> Unit,
        onRemoveButtonClicked: (position: Int) -> Unit,
        onArchiveButtonClicked: (position: Int) -> Unit,
        onActionStartOpen: (position: Int) -> Unit) : MyShowsViewHolder(binding.root)
    {
        init {
            binding.cardView.doOnClick {
                val position = bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) return@doOnClick
                onClicked(position)
            }

            binding.removeButton.doOnClick {
                val position = bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) return@doOnClick
                onRemoveButtonClicked(position)
            }

            binding.archiveButton.doOnClick {
                val position = bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) return@doOnClick
                onArchiveButtonClicked(position)
            }

            binding.swipeLayout.addSwipeListener(object : AbstractSwipeListener() {
                override fun onStartOpen(layout: SwipeLayout) {
                    val position = bindingAdapterPosition
                    if (position == RecyclerView.NO_POSITION) return
                    onActionStartOpen(position)
                }
            })
        }

        override fun closeSwipeActions() {
            binding.swipeLayout.close()
        }
    }

    class ShowViewHolder(
        val binding: MyShowLayoutBinding,
        onClicked: (position: Int) -> Unit,
        onRemoveButtonClicked: (position: Int) -> Unit,
        onArchiveButtonClicked: (position: Int) -> Unit,
        onUnarchiveButtonClicked: (position: Int) -> Unit,
        onActionStartOpen: (position: Int) -> Unit) : MyShowsViewHolder(binding.root)
    {
        init {
            binding.cardView.doOnClick {
                val position = bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) return@doOnClick
                onClicked(position)
            }

            binding.removeButton.doOnClick {
                val position = bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) return@doOnClick
                onRemoveButtonClicked(position)
            }

            binding.archiveButton.doOnClick {
                val position = bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) return@doOnClick
                onArchiveButtonClicked(position)
            }

            binding.unarchiveButton.doOnClick {
                val position = bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) return@doOnClick
                onUnarchiveButtonClicked(position)
            }

            binding.swipeLayout.addSwipeListener(object : AbstractSwipeListener() {
                override fun onStartOpen(layout: SwipeLayout) {
                    val position = bindingAdapterPosition
                    if (position == RecyclerView.NO_POSITION) return
                    onActionStartOpen(position)
                }
            })
        }

        override fun closeSwipeActions() {
            binding.swipeLayout.close()
        }
    }
}
