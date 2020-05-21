package dev.polek.episodetracker.discover

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.polek.episodetracker.R
import dev.polek.episodetracker.common.presentation.discover.model.DiscoverResultViewModel
import dev.polek.episodetracker.databinding.DiscoverResultLayoutBinding
import dev.polek.episodetracker.utils.doOnClick
import dev.polek.episodetracker.utils.layoutInflater

class DiscoverAdapter : RecyclerView.Adapter<DiscoverAdapter.ViewHolder>() {

    var listener: Listener? = null

    private var results: MutableList<DiscoverResultViewModel> = mutableListOf()

    fun setResults(results: List<DiscoverResultViewModel>) {
        this.results = results.toMutableList()
        notifyDataSetChanged()
    }

    fun updateResult(result: DiscoverResultViewModel) {
        val position = results.indexOfFirst { it.id == result.id }
        if (position >= 0) {
            results[position] = result
            notifyItemChanged(position)
        }
    }

    override fun getItemCount() = results.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DiscoverResultLayoutBinding.inflate(parent.layoutInflater, parent, false)
        return ViewHolder(
            binding,
            onResultClicked = { position ->
                listener?.onResultClicked(results[position])
            },
            onAddButtonClicked = { position ->
                val result = results[position]
                if (result.isInMyShows) {
                    listener?.onRemoveButtonClicked(result)
                } else {
                    listener?.onAddButtonClicked(result)
                }
            }
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.binding.name.text = result.name
        holder.binding.year.text = result.year.toString()
        holder.binding.network.text = ""
        holder.binding.overview.text = result.overview
        Glide.with(holder.itemView)
            .load(result.posterUrl)
            .into(holder.binding.image)

        holder.binding.addButtonProgress.isVisible = result.isAddInProgress
        if (result.isAddInProgress) {
            holder.binding.button.setImageDrawable(null)
        } else {
            val buttonIcon = if (result.isInMyShows) R.drawable.ic_minus else R.drawable.ic_add
            holder.binding.button.setImageResource(buttonIcon)
        }
    }

    class ViewHolder(
        val binding: DiscoverResultLayoutBinding,
        onResultClicked: (position: Int) -> Unit,
        onAddButtonClicked: (position: Int) -> Unit) : RecyclerView.ViewHolder(binding.root)
    {
        init {
            binding.root.doOnClick {
                val position = adapterPosition
                if (position == RecyclerView.NO_POSITION) return@doOnClick
                onResultClicked(position)
            }

            binding.button.doOnClick {
                val position = adapterPosition
                if (position == RecyclerView.NO_POSITION) return@doOnClick
                onAddButtonClicked(position)
            }
        }
    }

    interface Listener {
        fun onResultClicked(result: DiscoverResultViewModel)
        fun onAddButtonClicked(result: DiscoverResultViewModel)
        fun onRemoveButtonClicked(result: DiscoverResultViewModel)
    }
}
