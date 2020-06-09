package dev.polek.episodetracker.showdetails.episodes

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import dev.polek.episodetracker.R
import dev.polek.episodetracker.common.presentation.showdetails.model.EpisodeViewModel
import dev.polek.episodetracker.common.presentation.showdetails.model.SeasonViewModel
import dev.polek.episodetracker.databinding.EpisodeLayoutBinding
import dev.polek.episodetracker.databinding.SeasonLayoutBinding
import dev.polek.episodetracker.showdetails.episodes.SeasonAdapter.ViewHolder.EpisodeViewHolder
import dev.polek.episodetracker.showdetails.episodes.SeasonAdapter.ViewHolder.SeasonViewHolder
import dev.polek.episodetracker.utils.doOnClick
import dev.polek.episodetracker.utils.layoutInflater
import dev.polek.episodetracker.utils.loadImage

class SeasonAdapter(
    val season: SeasonViewModel,
    private val listener: Listener) : RecyclerView.Adapter<SeasonAdapter.ViewHolder>()
{
    fun reload() {
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (season.isExpanded) {
            1/* header */ + season.episodes.size
        } else {
            1/* header */
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> R.layout.season_layout
            else -> R.layout.episode_layout
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            R.layout.season_layout -> {
                SeasonViewHolder(
                    binding = SeasonLayoutBinding.inflate(parent.layoutInflater, parent, false),
                    onClicked = {
                        if (season.isExpanded) collapse() else expand()
                    },
                    onCheckedChanged = { isChecked ->
                        if (season.isWatched != isChecked) {
                            listener.onSeasonWatchedStateToggled(season)
                        }
                    }
                )
            }
            R.layout.episode_layout -> {
                EpisodeViewHolder(
                    binding = EpisodeLayoutBinding.inflate(parent.layoutInflater, parent, false),
                    onClicked = {},
                    onCheckedChanged = { position, isChecked ->
                        val episode = episodeAtPosition(position)
                        if (episode.isWatched != isChecked) {
                            listener.onEpisodeWatchedStateToggled(episode)
                        }
                    }
                )
            }
            else -> throw NotImplementedError("Unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        payloads.forEach { payload ->
            when (payload as Payload) {
                Payload.EXPANSION_STATE_CHANGED -> {
                    (holder as SeasonViewHolder).bindExpansionIcon(season.isExpanded)
                }
            }
        }

        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is SeasonViewHolder -> {
                holder.binding.title.text = season.name
                holder.binding.checkbox.isChecked = season.isWatched
                holder.binding.countText.text = season.watchedEpisodes
                holder.bindExpansionIcon(season.isExpanded)
            }
            is EpisodeViewHolder -> {
                val episode = episodeAtPosition(position)
                holder.binding.image.loadImage(episode.imageUrl)
                holder.binding.title.text = "${episode.number.episode}. ${episode.name}"
                holder.binding.subtitle.text = episode.airDate
                holder.binding.checkbox.isChecked = episode.isWatched

                val isLastEpisode = position == itemCount - 1
                holder.binding.divider.root.isVisible = !isLastEpisode
                holder.binding.fullDivider.root.isVisible = isLastEpisode
            }
        }
    }

    private fun episodeAtPosition(position: Int): EpisodeViewModel = season.episodes[position - 1]

    private fun expand() {
        season.isExpanded = true
        notifyItemChanged(0, Payload.EXPANSION_STATE_CHANGED)
        notifyItemRangeInserted(1, season.episodes.size)
    }

    private fun collapse() {
        season.isExpanded = false
        notifyItemChanged(0, Payload.EXPANSION_STATE_CHANGED)
        notifyItemRangeRemoved(1, season.episodes.size)
    }

    sealed class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        class SeasonViewHolder(
            onClicked: () -> Unit, val binding: SeasonLayoutBinding,
            onCheckedChanged: (isChecked: Boolean) -> Unit) : ViewHolder(binding.root)
        {
            init {
                binding.root.doOnClick {
                    onClicked()
                }

                binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
                    onCheckedChanged(isChecked)
                }
            }

            fun bindExpansionIcon(isExpanded: Boolean) {
                val arrowIcon = if (isExpanded) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down
                binding.title.setCompoundDrawablesWithIntrinsicBounds(arrowIcon, 0, 0, 0)
            }
        }
        class EpisodeViewHolder(
            onClicked: (position: Int) -> Unit,
            onCheckedChanged: (position: Int, isChecked: Boolean) -> Unit,
            val binding: EpisodeLayoutBinding) : ViewHolder(binding.root)
        {
            init {
                binding.root.doOnClick {
                    val position = bindingAdapterPosition
                    if (position == RecyclerView.NO_POSITION) return@doOnClick
                    onClicked(position)
                }

                binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
                    val position = bindingAdapterPosition
                    if (position == RecyclerView.NO_POSITION) return@setOnCheckedChangeListener
                    onCheckedChanged(position, isChecked)
                }
            }
        }
    }

    interface Listener {
        fun onSeasonWatchedStateToggled(season: SeasonViewModel)
        fun onEpisodeWatchedStateToggled(episode: EpisodeViewModel)
    }

    private enum class Payload {
        EXPANSION_STATE_CHANGED
    }
}
