package dev.polek.episodetracker.myshows

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.polek.episodetracker.common.presentation.myshows.model.MyShowsListItem
import dev.polek.episodetracker.databinding.MyShowLayoutBinding
import dev.polek.episodetracker.utils.doOnClick
import dev.polek.episodetracker.utils.layoutInflater
import dev.polek.episodetracker.utils.loadImage

class ShowsAdapter : RecyclerView.Adapter<ShowsAdapter.ViewHolder>() {

    var listener: MyShowsAdapterListener? = null

    var shows: List<MyShowsListItem.ShowViewModel> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = shows.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MyShowLayoutBinding.inflate(parent.layoutInflater, parent, false)
        return ViewHolder(binding, onClicked = { position ->
            listener?.onShowClicked(shows[position])
        })
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val show = shows[position]
        holder.binding.name.text = show.name
        holder.binding.image.loadImage(show.backdropUrl)
    }

    class ViewHolder(
        val binding: MyShowLayoutBinding,
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
