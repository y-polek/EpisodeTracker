package dev.polek.episodetracker.showdetails.about

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.polek.episodetracker.common.presentation.showdetails.model.CastMemberViewModel
import dev.polek.episodetracker.databinding.CastMemberLayoutBinding
import dev.polek.episodetracker.utils.doOnClick
import dev.polek.episodetracker.utils.layoutInflater
import dev.polek.episodetracker.utils.loadImage

class CastAdapter(private val onClicked: (castMember: CastMemberViewModel) -> Unit) : RecyclerView.Adapter<CastAdapter.ViewHolder>() {

    var cast: List<CastMemberViewModel> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = cast.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CastMemberLayoutBinding.inflate(parent.layoutInflater, parent, false)
        return ViewHolder(binding, onClicked = { position ->
            onClicked(cast[position])
        })
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val castMember = cast[position]
        holder.binding.image.loadImage(castMember.portraitImageUrl)
        holder.binding.actorName.text = castMember.name
        holder.binding.characterName.text = castMember.character
    }

    class ViewHolder(
        val binding: CastMemberLayoutBinding,
        val onClicked: (position: Int) -> Unit) : RecyclerView.ViewHolder(binding.root)
    {
        init {
            binding.cardView.doOnClick {
                val position = bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) return@doOnClick

                onClicked(position)
            }
        }
    }
}
