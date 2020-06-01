package dev.polek.episodetracker.showdetails.episodes

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.polek.episodetracker.databinding.SeasonLayoutBinding
import dev.polek.episodetracker.utils.layoutInflater

class SeasonAdapter : RecyclerView.Adapter<SeasonAdapter.ViewHolder>() {

    override fun getItemCount() = 5

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SeasonLayoutBinding.inflate(parent.layoutInflater, parent, false)
        return ViewHolder.SeasonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as ViewHolder.SeasonViewHolder).binding.name.text = position.toString()
    }

    sealed class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        class SeasonViewHolder(val binding: SeasonLayoutBinding) : ViewHolder(binding.root)
    }
}
