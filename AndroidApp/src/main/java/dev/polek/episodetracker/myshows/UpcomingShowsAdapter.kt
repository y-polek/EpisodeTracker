package dev.polek.episodetracker.myshows

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.polek.episodetracker.common.presentation.myshows.model.MyShowsListItem
import dev.polek.episodetracker.databinding.UpcomingShowLayoutBinding
import dev.polek.episodetracker.utils.doOnClick
import dev.polek.episodetracker.utils.layoutInflater
import dev.polek.episodetracker.utils.loadImage

class UpcomingShowsAdapter : RecyclerView.Adapter<UpcomingShowsAdapter.ViewHolder>() {

    var listener: MyShowsAdapterListener? = null

    var shows: List<MyShowsListItem.UpcomingShowViewModel> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = shows.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = UpcomingShowLayoutBinding.inflate(parent.layoutInflater, parent, false)
        return ViewHolder(binding, onClicked = { position ->
            listener?.onShowClicked(shows[position])
        })
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val show = shows[position]
        holder.binding.name.text = show.name
        holder.binding.episodeName.text = show.episodeName
        holder.binding.episodeNumber.text = show.episodeNumber
        holder.binding.timeLeft.text = show.timeLeft
        holder.binding.image.loadImage(show.backdropUrl)
    }

    class ViewHolder(
        val binding: UpcomingShowLayoutBinding,
        onClicked: (position: Int) -> Unit) : RecyclerView.ViewHolder(binding.root)
    {
        init {
            itemView.doOnClick {
                val position = bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) return@doOnClick

                onClicked(position)
            }
        }
    }
}
