package dev.polek.episodetracker.myshows

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.polek.episodetracker.R
import dev.polek.episodetracker.common.presentation.myshows.model.MyShowsListItem
import dev.polek.episodetracker.databinding.MyShowLayoutBinding
import dev.polek.episodetracker.utils.layoutInflater
import dev.polek.episodetracker.utils.loadImage

class ShowsAdapter : RecyclerView.Adapter<MyShowsViewHolder>() {

    var listener: MyShowsAdapterListener? = null

    var shows: List<MyShowsListItem.ShowViewModel> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = shows.size

    override fun getItemViewType(position: Int): Int {
        return R.layout.my_show_layout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyShowsViewHolder {
        val binding = MyShowLayoutBinding.inflate(parent.layoutInflater, parent, false)
        return MyShowsViewHolder.ShowViewHolder(binding, onClicked = { position ->
            listener?.onShowClicked(shows[position])
        })
    }

    override fun onBindViewHolder(holder: MyShowsViewHolder, position: Int) {
        when (holder) {
            is MyShowsViewHolder.ShowViewHolder -> {
                val show = shows[position]
                holder.binding.name.text = show.name
                holder.binding.image.loadImage(show.backdropUrl)
            }
        }
    }
}
