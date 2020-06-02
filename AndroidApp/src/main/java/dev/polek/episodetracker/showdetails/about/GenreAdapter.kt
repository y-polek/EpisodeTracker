package dev.polek.episodetracker.showdetails.about

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.polek.episodetracker.databinding.GenreCardBinding
import dev.polek.episodetracker.utils.layoutInflater

class GenreAdapter : RecyclerView.Adapter<GenreAdapter.ViewHolder>() {

    var genres: List<String> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = genres.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GenreCardBinding.inflate(parent.layoutInflater)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.textView.text = genres[position]
    }

    class ViewHolder(val binding: GenreCardBinding) : RecyclerView.ViewHolder(binding.root)
}
