package dev.polek.episodetracker.myshows

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.polek.episodetracker.R
import dev.polek.episodetracker.databinding.GroupHeaderLayoutBinding
import dev.polek.episodetracker.databinding.MyShowLayoutBinding
import dev.polek.episodetracker.databinding.UpcomingShowLayoutBinding
import dev.polek.episodetracker.myshows.MyShowsAdapter.ViewHolder.*
import dev.polek.episodetracker.utils.doOnClick
import dev.polek.episodetracker.utils.layoutInflater

class MyShowsAdapter : RecyclerView.Adapter<MyShowsAdapter.ViewHolder>() {


    override fun getItemCount() = 0

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
        TODO("not implemented")
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
}
