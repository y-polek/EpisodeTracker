package dev.polek.episodetracker.myshows

import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import dev.polek.episodetracker.R
import dev.polek.episodetracker.common.presentation.myshows.model.MyShowsListItem
import dev.polek.episodetracker.databinding.GroupHeaderLayoutBinding
import dev.polek.episodetracker.databinding.MyShowLayoutBinding
import dev.polek.episodetracker.utils.layoutInflater
import dev.polek.episodetracker.utils.loadImage

class ShowsAdapter(
    @StringRes val titleRes: Int,
    isExpanded: Boolean,
    val onShowClicked: (show: MyShowsListItem.ShowViewModel) -> Unit,
    val onExpandStateChanged: (isExpanded: Boolean) -> Unit) : RecyclerView.Adapter<MyShowsViewHolder>()
{
    var shows: List<MyShowsListItem.ShowViewModel> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var isExpanded: Boolean = isExpanded
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int {
        return when {
            shows.isEmpty() -> 0
            isExpanded -> 1/*header*/ + shows.size
            else -> 1/*header*/
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> R.layout.group_header_layout
            else -> R.layout.my_show_layout
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyShowsViewHolder {
        return when (viewType) {
            R.layout.group_header_layout -> {
                val binding = GroupHeaderLayoutBinding.inflate(parent.layoutInflater, parent, false)
                MyShowsViewHolder.GroupHeaderViewHolder(binding, onClicked = { position ->
                    isExpanded = !isExpanded
                    onExpandStateChanged(isExpanded)
                })
            }
            R.layout.my_show_layout -> {
                val binding = MyShowLayoutBinding.inflate(parent.layoutInflater, parent, false)
                return MyShowsViewHolder.ShowViewHolder(binding, onClicked = { position ->
                    onShowClicked(shows[position])
                })
            }
            else -> throw IllegalStateException("Unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: MyShowsViewHolder, position: Int) {
        when (holder) {
            is MyShowsViewHolder.GroupHeaderViewHolder -> {
                holder.binding.name.setText(titleRes)
                val arrowIcon = if (isExpanded) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down
                holder.binding.name.setCompoundDrawablesWithIntrinsicBounds(0, 0, arrowIcon, 0)
            }
            is MyShowsViewHolder.ShowViewHolder -> {
                val show = shows[position - 1]
                holder.binding.name.text = show.name
                holder.binding.image.loadImage(show.backdropUrl)
            }
        }
    }
}
