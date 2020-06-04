package dev.polek.episodetracker.showdetails.about

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.polek.episodetracker.common.presentation.showdetails.model.TrailerViewModel
import dev.polek.episodetracker.databinding.TrailerLayoutBinding
import dev.polek.episodetracker.utils.doOnClick
import dev.polek.episodetracker.utils.layoutInflater
import dev.polek.episodetracker.utils.loadImage

class TrailerAdapter(val onPlayClicked: (trailer: TrailerViewModel) -> Unit) : RecyclerView.Adapter<TrailerAdapter.ViewHolder>() {

    var trailers: List<TrailerViewModel> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = trailers.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TrailerLayoutBinding.inflate(parent.layoutInflater, parent, false)
        return ViewHolder(binding, onPlayClicked = { position ->
            onPlayClicked(trailers[position])
        })
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val trailer = trailers[position]
        holder.binding.image.loadImage(trailer.previewImageUrl)
        holder.binding.name.text = trailer.name
    }

    class ViewHolder(
        val binding: TrailerLayoutBinding,
        val onPlayClicked: (position: Int) -> Unit) : RecyclerView.ViewHolder(binding.root)
    {
        init {
            val listener: (View) -> Unit = {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onPlayClicked(position)
                }
            }
            binding.cardView.doOnClick(listener)
            binding.playButton.doOnClick(listener)
        }
    }
}
