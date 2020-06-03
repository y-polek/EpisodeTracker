package dev.polek.episodetracker.showdetails.about

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import dev.polek.episodetracker.R
import dev.polek.episodetracker.common.presentation.showdetails.model.RecommendationViewModel
import dev.polek.episodetracker.databinding.RecommendationLayoutBinding
import dev.polek.episodetracker.utils.doOnClick
import dev.polek.episodetracker.utils.layoutInflater
import dev.polek.episodetracker.utils.loadImage

class RecommendationAdapter(
    val onClicked: (recommendation: RecommendationViewModel) -> Unit,
    val onAddButtonClicked: (recommendation: RecommendationViewModel) -> Unit,
    val onRemoveButtonClicked: (recommendation: RecommendationViewModel) -> Unit) : RecyclerView.Adapter<RecommendationAdapter.ViewHolder>()
{
    private var recommendations: MutableList<RecommendationViewModel> = mutableListOf()

    fun setRecommendations(recommendations: List<RecommendationViewModel>) {
        this.recommendations = recommendations.toMutableList()
        notifyDataSetChanged()
    }

    fun updateRecommendation(recommendation: RecommendationViewModel) {
        val position = recommendations.indexOfFirst { it.showId == recommendation.showId }
        if (position >= 0) {
            recommendations[position] = recommendation
            notifyItemChanged(position, Payload.IN_MY_SHOWS_STATUS)
        }
    }

    override fun getItemCount(): Int = recommendations.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecommendationLayoutBinding.inflate(parent.layoutInflater, parent, false)
        return ViewHolder(
            binding,
            onClicked = { position ->
                onClicked(recommendations[position])
            },
            onAddButtonClicked = { position ->
                val show = recommendations[position]
                if (show.isInMyShows) {
                    onRemoveButtonClicked(show)
                } else {
                    onAddButtonClicked(show)
                }
            }
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val show = recommendations[position]

        holder.binding.image.loadImage(show.imageUrl)
        holder.binding.title.text = show.name
        holder.binding.subtitle.text = show.subhead

        bindAddButton(holder.binding, position)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        payloads.forEach { payload ->
            when (payload) {
                Payload.IN_MY_SHOWS_STATUS -> {
                    bindAddButton(holder.binding, position)
                }
            }
        }

        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    private fun bindAddButton(binding: RecommendationLayoutBinding, position: Int) {
        val show = recommendations[position]

        binding.addButtonProgress.isVisible = show.isAddInProgress

        if (show.isAddInProgress) {
            binding.addButton.setImageDrawable(null)
        } else {
            val buttonIcon = if (show.isInMyShows) R.drawable.ic_minus else R.drawable.ic_add
            binding.addButton.setImageResource(buttonIcon)
        }
    }

    class ViewHolder(
        val binding: RecommendationLayoutBinding,
        onClicked: (position: Int) -> Unit,
        onAddButtonClicked: (position: Int) -> Unit) : RecyclerView.ViewHolder(binding.root)
    {
        init {
            binding.cardView.doOnClick {
                val position = bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) return@doOnClick

                onClicked(position)
            }
            binding.addButton.doOnClick {
                val position = bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) return@doOnClick

                onAddButtonClicked(position)
            }
        }
    }

    private enum class Payload {
        IN_MY_SHOWS_STATUS
    }
}
