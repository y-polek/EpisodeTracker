package dev.polek.episodetracker.myshows

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import dev.polek.episodetracker.databinding.GroupHeaderLayoutBinding
import dev.polek.episodetracker.databinding.MyShowLayoutBinding
import dev.polek.episodetracker.databinding.UpcomingShowLayoutBinding
import dev.polek.episodetracker.utils.doOnClick

sealed class MyShowsViewHolder(
    itemView: View,
    onClicked: (position: Int) -> Unit) : RecyclerView.ViewHolder(itemView)
{
    init {
        itemView.doOnClick {
            val position = bindingAdapterPosition
            if (position == RecyclerView.NO_POSITION) return@doOnClick
            onClicked(position)
        }
    }

    class GroupHeaderViewHolder(
        val binding: GroupHeaderLayoutBinding,
        onClicked: (position: Int) -> Unit) : MyShowsViewHolder(binding.root, onClicked)

    class UpcomingShowViewHolder(
        val binding: UpcomingShowLayoutBinding,
        onClicked: (position: Int) -> Unit) : MyShowsViewHolder(binding.root, onClicked)

    class ShowViewHolder(
        val binding: MyShowLayoutBinding,
        onClicked: (position: Int) -> Unit) : MyShowsViewHolder(binding.root, onClicked)
}
