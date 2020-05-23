package dev.polek.episodetracker.towatch

import android.text.Html
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.polek.episodetracker.R
import dev.polek.episodetracker.common.presentation.towatch.ToWatchShowViewModel
import dev.polek.episodetracker.databinding.ToWatchShowLayoutBinding
import dev.polek.episodetracker.utils.layoutInflater

class ToWatchAdapter : RecyclerView.Adapter<ToWatchAdapter.ViewHolder>() {

    var shows: List<ToWatchShowViewModel> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = shows.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ToWatchShowLayoutBinding.inflate(parent.layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val show = shows[position]
        val binding = holder.binding

        binding.title.text = show.name

        @Suppress("DEPRECATION")
        binding.subtitle.text = Html.fromHtml("<b>${show.nextEpisodeNumberText}</b> | ${show.nextEpisodeName}")

        binding.episodesCount.text = show.episodeCount.toString()
        binding.episodesLabel.text = holder.itemView.resources.getQuantityString(R.plurals.episodes, show.episodeCount)

        binding.specialsBadge.isVisible = show.isSpecials


        Glide.with(holder.itemView)
            .load(show.imageUrl)
            .into(binding.image)
    }

    class ViewHolder(val binding: ToWatchShowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}
