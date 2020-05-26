package dev.polek.episodetracker.myshows

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.polek.episodetracker.R
import dev.polek.episodetracker.common.presentation.myshows.model.MyShowsListItem
import dev.polek.episodetracker.databinding.GroupHeaderLayoutBinding
import dev.polek.episodetracker.databinding.MyShowLayoutBinding
import dev.polek.episodetracker.databinding.UpcomingShowLayoutBinding
import dev.polek.episodetracker.myshows.MyShowsAdapter.ViewHolder.*
import dev.polek.episodetracker.utils.doOnClick
import dev.polek.episodetracker.utils.layoutInflater
import dev.polek.episodetracker.utils.loadImage

class MyShowsAdapter(
    isLastWeekExpanded: Boolean,
    isUpcomingExpanded: Boolean,
    isTbaExpanded: Boolean,
    isEndedExpanded: Boolean,
    isArchivedExpanded: Boolean) : RecyclerView.Adapter<MyShowsAdapter.ViewHolder>()
{
    val model = MyShowsModel(
        isLastWeekExpanded = isLastWeekExpanded,
        isUpcomingExpanded = isUpcomingExpanded,
        isTbaExpanded = isTbaExpanded,
        isEndedExpanded = isEndedExpanded,
        isArchivedExpanded = isArchivedExpanded,
        onModelModified = {
            notifyDataSetChanged()
        }
    )

    var listener: Listener? = null

    override fun getItemCount() = model.count()

    override fun getItemViewType(position: Int) = model.viewType(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        R.id.view_type_group -> {
            GroupHeaderViewHolder(
                GroupHeaderLayoutBinding.inflate(parent.layoutInflater, parent, false),
                onClicked = { position ->
                    val section = model.sectionAt(position)
                    when (section) {
                        model.lastWeekSection -> {
                            val isExpanded = listener?.onLastWeekSectionClicked() ?: return@GroupHeaderViewHolder
                            model.lastWeekSection.isExpanded = isExpanded
                        }
                        model.upcomingSection -> {
                            val isExpanded = listener?.onUpcomingSectionClicked() ?: return@GroupHeaderViewHolder
                            model.upcomingSection.isExpanded = isExpanded
                        }
                        model.tbaSection -> {
                            val isExpanded = listener?.onTbaSectionClicked() ?: return@GroupHeaderViewHolder
                            model.tbaSection.isExpanded = isExpanded
                        }
                        model.endedSection -> {
                            val isExpanded = listener?.onEndedSectionClicked() ?: return@GroupHeaderViewHolder
                            model.endedSection.isExpanded = isExpanded
                        }
                        model.archivedSection -> {
                            val isExpanded = listener?.onArchivedSectionClicked() ?: return@GroupHeaderViewHolder
                            model.archivedSection.isExpanded = isExpanded
                        }
                    }
                    notifyItemChanged(section.positions.first)
                })
        }
        R.id.view_type_upcoming_show -> {
            UpcomingShowViewHolder(
                UpcomingShowLayoutBinding.inflate(parent.layoutInflater, parent, false),
                onClicked = { position ->
                    val show = model.sectionAt(position).showAt(position)
                    listener?.onShowClicked(show as MyShowsListItem.UpcomingShowViewModel)
                })
        }
        R.id.view_type_show -> {
            ShowViewHolder(
                MyShowLayoutBinding.inflate(parent.layoutInflater, parent, false),
                onClicked = { position ->
                    val show = model.sectionAt(position).showAt(position)
                    listener?.onShowClicked(show as MyShowsListItem.ShowViewModel)
                })
        }
        else -> throw NotImplementedError("Unknown view type: $viewType")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val section = model.sectionAt(position)

        when (holder) {
            is GroupHeaderViewHolder -> {
                holder.binding.name.setText(section.nameRes)
                val arrowIcon = if (section.isExpanded) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down
                holder.binding.name.setCompoundDrawablesWithIntrinsicBounds(0, 0, arrowIcon, 0)
            }
            is UpcomingShowViewHolder -> {
                val show = section.showAt(position) as MyShowsListItem.UpcomingShowViewModel
                holder.binding.name.text = show.name
                holder.binding.episodeName.text = show.episodeName
                holder.binding.episodeNumber.text = show.episodeNumber
                holder.binding.timeLeft.text = show.timeLeft
                holder.binding.image.loadImage(show.backdropUrl)
            }
            is ShowViewHolder -> {
                val show = section.showAt(position) as MyShowsListItem.ShowViewModel
                holder.binding.name.text = show.name
                holder.binding.image.loadImage(show.backdropUrl)
            }
        }
    }

    sealed class ViewHolder(itemView: View, onClicked: (position: Int) -> Unit) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.doOnClick {
                val position = adapterPosition
                if (position == RecyclerView.NO_POSITION) return@doOnClick

                onClicked(position)
            }
        }

        class GroupHeaderViewHolder(val binding: GroupHeaderLayoutBinding, onClicked: (position: Int) -> Unit) : ViewHolder(binding.root, onClicked)

        class UpcomingShowViewHolder(val binding: UpcomingShowLayoutBinding, onClicked: (position: Int) -> Unit) : ViewHolder(binding.root, onClicked)

        class ShowViewHolder(val binding: MyShowLayoutBinding, onClicked: (position: Int) -> Unit) : ViewHolder(binding.root, onClicked)
    }

    interface Listener {
        fun onShowClicked(show: MyShowsListItem.ShowViewModel)
        fun onLastWeekSectionClicked(): Boolean
        fun onUpcomingSectionClicked(): Boolean
        fun onTbaSectionClicked(): Boolean
        fun onEndedSectionClicked(): Boolean
        fun onArchivedSectionClicked(): Boolean
    }
}
