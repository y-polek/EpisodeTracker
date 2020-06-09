package dev.polek.episodetracker.showdetails.episodes

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.polek.episodetracker.R
import dev.polek.episodetracker.common.presentation.showdetails.model.EpisodeViewModel
import dev.polek.episodetracker.common.presentation.showdetails.model.SeasonViewModel
import dev.polek.episodetracker.databinding.EpisodeLayoutBinding
import dev.polek.episodetracker.databinding.SeasonLayoutBinding
import dev.polek.episodetracker.showdetails.episodes.SeasonAdapter.ViewHolder.EpisodeViewHolder
import dev.polek.episodetracker.showdetails.episodes.SeasonAdapter.ViewHolder.SeasonViewHolder
import dev.polek.episodetracker.utils.layoutInflater
import dev.polek.episodetracker.utils.loadImage

class SeasonAdapter(private val season: SeasonViewModel) : RecyclerView.Adapter<SeasonAdapter.ViewHolder>() {

    override fun getItemCount() = 1/* header */ + season.episodes.size

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> R.layout.season_layout
            else -> R.layout.episode_layout
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            R.layout.season_layout -> {
                SeasonViewHolder(SeasonLayoutBinding.inflate(parent.layoutInflater, parent, false))
            }
            R.layout.episode_layout -> {
                EpisodeViewHolder(EpisodeLayoutBinding.inflate(parent.layoutInflater, parent, false))
            }
            else -> throw NotImplementedError("Unknown view type: $viewType")
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is SeasonViewHolder -> {
                holder.binding.title.text = season.name
                holder.binding.checkbox.isChecked = season.isWatched
            }
            is EpisodeViewHolder -> {
                val episode = episodeAtPosition(position)
                holder.binding.image.loadImage(episode.imageUrl)
                holder.binding.title.text = "${episode.number.episode}. ${episode.name}"
                holder.binding.subtitle.text = episode.airDate
                holder.binding.checkbox.isChecked = episode.isWatched
            }
        }
    }

    private fun episodeAtPosition(position: Int): EpisodeViewModel = season.episodes[position - 1]

    sealed class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        class SeasonViewHolder(val binding: SeasonLayoutBinding) : ViewHolder(binding.root)
        class EpisodeViewHolder(val binding: EpisodeLayoutBinding) : ViewHolder(binding.root)
    }
}
