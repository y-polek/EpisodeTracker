package dev.polek.episodetracker.myshows

import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import dev.polek.episodetracker.R
import dev.polek.episodetracker.common.presentation.myshows.model.MyShowsListItem
import dev.polek.episodetracker.databinding.GroupHeaderLayoutBinding
import dev.polek.episodetracker.databinding.MyShowLayoutBinding
import dev.polek.episodetracker.databinding.UpcomingShowLayoutBinding
import dev.polek.episodetracker.myshows.MyShowsAdapter.ViewHolder.*
import dev.polek.episodetracker.utils.doOnClick
import dev.polek.episodetracker.utils.layoutInflater
import dev.polek.episodetracker.utils.loadImage

class MyShowsAdapter : RecyclerView.Adapter<MyShowsAdapter.ViewHolder>() {

    val model = Model(onModelModified = {
        notifyDataSetChanged()
    })

    override fun getItemCount() = model.count()

    override fun getItemViewType(position: Int) = model.viewType(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        R.id.view_type_group -> {
            GroupHeaderViewHolder(
                GroupHeaderLayoutBinding.inflate(parent.layoutInflater, parent, false),
                onExpandClicked = { position ->

                })
        }
        R.id.view_type_upcoming_show -> {
            UpcomingShowViewHolder(
                UpcomingShowLayoutBinding.inflate(parent.layoutInflater, parent, false))
        }
        R.id.view_type_show -> {
            ShowViewHolder(
                MyShowLayoutBinding.inflate(parent.layoutInflater, parent, false))
        }
        else -> throw NotImplementedError("Unknown view type: $viewType")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is UpcomingShowViewHolder -> {
                val show = model.itemAt(position) as MyShowsListItem.UpcomingShowViewModel
                holder.binding.name.text = show.name
                holder.binding.episodeName.text = show.episodeName
                holder.binding.episodeNumber.text = show.episodeNumber
                holder.binding.timeLeft.text = show.timeLeft
                holder.binding.image.loadImage(show.backdropUrl)
            }
            is ShowViewHolder -> {
                val show = model.itemAt(position) as MyShowsListItem.ShowViewModel
                holder.binding.name.text = show.name
                holder.binding.image.loadImage(show.backdropUrl)
            }
        }
    }

    sealed class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        class GroupHeaderViewHolder(
            val binding: GroupHeaderLayoutBinding,
            onExpandClicked: (position: Int) -> Unit) : ViewHolder(binding.root)
        {
            init {
                binding.root.doOnClick {
                    val position = adapterPosition
                    if (position == RecyclerView.NO_POSITION) return@doOnClick

                    onExpandClicked(position)
                }
            }
        }

        class UpcomingShowViewHolder(val binding: UpcomingShowLayoutBinding) : ViewHolder(binding.root)

        class ShowViewHolder(val binding: MyShowLayoutBinding) : ViewHolder(binding.root)
    }

    class Model(private val onModelModified: () -> Unit) {

        var lastWeekShows: List<MyShowsListItem.UpcomingShowViewModel> = emptyList()
            set(value) {
                field = value
                onModelModified()
            }

        var upcomingShows: List<MyShowsListItem.UpcomingShowViewModel> = emptyList()
            set(value) {
                field = value
                onModelModified()
            }

        var tbaShows: List<MyShowsListItem.ShowViewModel> = emptyList()
            set(value) {
                field = value
                onModelModified()
            }

        var endedShows: List<MyShowsListItem.ShowViewModel> = emptyList()
            set(value) {
                field = value
                onModelModified()
            }

        var archivedShow: List<MyShowsListItem.ShowViewModel> = emptyList()
            set(value) {
                field = value
                onModelModified()
            }

        fun count(): Int {
            return lastWeekShows.size + upcomingShows.size + tbaShows.size + endedShows.size + archivedShow.size
        }

        @IdRes
        fun viewType(position: Int): Int {
            return when (itemAt(position)) {
                is MyShowsListItem.UpcomingShowViewModel -> R.id.view_type_upcoming_show
                is MyShowsListItem.ShowViewModel -> R.id.view_type_show
            }
        }

        fun itemAt(position: Int): MyShowsListItem {
            return when {
                position < lastWeekShows.size -> {
                    lastWeekShows[position]
                }
                position < lastWeekShows.size + upcomingShows.size -> {
                    upcomingShows[position - lastWeekShows.size]
                }
                position < lastWeekShows.size + upcomingShows.size + tbaShows.size -> {
                    tbaShows[position - lastWeekShows.size - upcomingShows.size]
                }
                position < lastWeekShows.size + upcomingShows.size + tbaShows.size + endedShows.size -> {
                    endedShows[position - lastWeekShows.size - upcomingShows.size - tbaShows.size]
                }
                else -> {
                    archivedShow[position - lastWeekShows.size - upcomingShows.size - tbaShows.size - endedShows.size]
                }
            }
        }
    }
}
