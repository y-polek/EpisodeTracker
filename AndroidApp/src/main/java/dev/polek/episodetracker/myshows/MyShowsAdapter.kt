package dev.polek.episodetracker.myshows

import android.net.Uri
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.polek.episodetracker.R
import dev.polek.episodetracker.databinding.MyShowLayoutBinding
import dev.polek.episodetracker.utils.layoutInflater
import java.net.URL

class MyShowsAdapter : RecyclerView.Adapter<MyShowsAdapter.ViewHolder>() {

    override fun getItemCount() = 10

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: MyShowLayoutBinding = MyShowLayoutBinding.inflate(parent.layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(Uri.parse("https://image.tmdb.org/t/p/w1066_and_h600_bestv2/s3kVP6R3LbJvvoPnDQEcJNEH2d0.jpg"))
            .into(holder.binding.image)
    }

    class ViewHolder(val binding: MyShowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}
