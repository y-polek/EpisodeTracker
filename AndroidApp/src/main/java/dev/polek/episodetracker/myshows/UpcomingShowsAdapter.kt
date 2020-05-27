package dev.polek.episodetracker.myshows

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.polek.episodetracker.R
import dev.polek.episodetracker.common.presentation.myshows.model.MyShowsListItem
import dev.polek.episodetracker.databinding.UpcomingShowLayoutBinding
import dev.polek.episodetracker.utils.layoutInflater
import dev.polek.episodetracker.utils.loadImage

class UpcomingShowsAdapter : RecyclerView.Adapter<MyShowsViewHolder>() {

    var listener: MyShowsAdapterListener? = null

    var shows: List<MyShowsListItem.UpcomingShowViewModel> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = shows.size

    override fun getItemViewType(position: Int): Int {
        return R.layout.upcoming_show_layout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyShowsViewHolder {
        val binding = UpcomingShowLayoutBinding.inflate(parent.layoutInflater, parent, false)
        return MyShowsViewHolder.UpcomingShowViewHolder(binding, onClicked = { position ->
            listener?.onShowClicked(shows[position])
        })
    }

    override fun onBindViewHolder(holder: MyShowsViewHolder, position: Int) {
        when (holder) {
            is MyShowsViewHolder.UpcomingShowViewHolder -> {
                val show = shows[position]
                holder.binding.name.text = show.name
                holder.binding.episodeName.text = show.episodeName
                holder.binding.episodeNumber.text = show.episodeNumber
                holder.binding.timeLeft.text = show.timeLeft
                holder.binding.image.loadImage(show.backdropUrl)
            }
        }
    }
}
