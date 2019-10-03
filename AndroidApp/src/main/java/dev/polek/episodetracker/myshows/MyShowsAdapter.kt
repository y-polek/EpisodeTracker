package dev.polek.episodetracker.myshows

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.polek.episodetracker.R
import dev.polek.episodetracker.databinding.GroupHeaderLayoutBinding
import dev.polek.episodetracker.databinding.MyShowLayoutBinding
import dev.polek.episodetracker.myshows.MyShowsAdapter.ViewHolder.*
import dev.polek.episodetracker.myshows.model.MyShowsListItem
import dev.polek.episodetracker.myshows.model.MyShowsListItem.*
import dev.polek.episodetracker.utils.layoutInflater

class MyShowsAdapter : RecyclerView.Adapter<MyShowsAdapter.ViewHolder>() {

    var items: List<MyShowsListItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = when (items[position]) {
        is GroupViewModel -> R.id.view_type_group
        is UpcomingShowViewModel -> R.id.view_type_upcoming_show
        is ShowViewModel -> R.id.view_type_show
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        R.id.view_type_group -> {
            GroupHeaderViewHolder(
                GroupHeaderLayoutBinding.inflate(parent.layoutInflater, parent, false))
        }
        R.id.view_type_upcoming_show -> {
            UpcomingShowViewHolder(
                MyShowLayoutBinding.inflate(parent.layoutInflater, parent, false))
        }
        R.id.view_type_show -> {
            ShowViewHolder(
                MyShowLayoutBinding.inflate(parent.layoutInflater, parent, false))
        }
        else -> throw NotImplementedError("Unknown view type: $viewType")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is GroupHeaderViewHolder -> {
                val group = items[position] as GroupViewModel
                holder.binding.name.text = group.name
                holder.binding.name.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    if (group.expanded) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down,
                    0)
            }
            is UpcomingShowViewHolder -> {
                val show = items[position] as UpcomingShowViewModel
                Glide.with(holder.itemView)
                    .load(Uri.parse(show.backdropUrl))
                    .into(holder.binding.image)
            }
            is ShowViewHolder -> {
                val show = items[position] as ShowViewModel
                Glide.with(holder.itemView)
                    .load(Uri.parse(show.backdropUrl))
                    .into(holder.binding.image)
            }
        }
    }

    sealed class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        class GroupHeaderViewHolder(val binding: GroupHeaderLayoutBinding) : ViewHolder(binding.root)

        class UpcomingShowViewHolder(val binding: MyShowLayoutBinding) : ViewHolder(binding.root)

        class ShowViewHolder(val binding: MyShowLayoutBinding) : ViewHolder(binding.root)
    }
}
