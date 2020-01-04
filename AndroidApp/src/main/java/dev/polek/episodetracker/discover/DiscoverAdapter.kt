package dev.polek.episodetracker.discover

import android.net.Uri
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.polek.episodetracker.databinding.DiscoverResultLayoutBinding
import dev.polek.episodetracker.discover.model.DiscoverResultViewModel
import dev.polek.episodetracker.utils.doOnClick
import dev.polek.episodetracker.utils.layoutInflater

class DiscoverAdapter : RecyclerView.Adapter<DiscoverAdapter.ViewHolder>() {

    var results: List<DiscoverResultViewModel> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = results.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DiscoverResultLayoutBinding.inflate(parent.layoutInflater, parent, false)
        return ViewHolder(
            binding,
            onResultClicked = { position ->

            })
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.binding.name.text = result.name
        holder.binding.year.text = result.year.toString()
        holder.binding.network.text = ""
        holder.binding.overview.text = result.overview
        Glide.with(holder.itemView)
            .load(Uri.parse(result.posterUrl))
            .into(holder.binding.image)
    }

    class ViewHolder(
        val binding: DiscoverResultLayoutBinding,
        onResultClicked: (position: Int) -> Unit) : RecyclerView.ViewHolder(binding.root)
    {
        init {
            binding.root.doOnClick {
                val position = adapterPosition
                if (position == RecyclerView.NO_POSITION) return@doOnClick

                onResultClicked(position)
            }
        }
    }
}
