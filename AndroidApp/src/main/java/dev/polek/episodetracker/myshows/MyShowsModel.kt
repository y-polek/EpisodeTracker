package dev.polek.episodetracker.myshows

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import dev.polek.episodetracker.R
import dev.polek.episodetracker.common.presentation.myshows.model.MyShowsListItem

class MyShowsModel(
    isLastWeekExpanded: Boolean,
    isUpcomingExpanded: Boolean,
    isTbaExpanded: Boolean,
    isEndedExpanded: Boolean,
    isArchivedExpanded: Boolean,
    private val onModelModified: () -> Unit)
{
    val lastWeekSection = Section(R.string.my_shows_last_week, isLastWeekExpanded)
    val upcomingSection = Section(R.string.my_shows_upcoming, isUpcomingExpanded)
    val tbaSection = Section(R.string.my_shows_tba, isTbaExpanded)
    val endedSection = Section(R.string.my_shows_ended, isEndedExpanded)
    val archivedSection = Section(R.string.my_shows_archived, isArchivedExpanded)

    fun setLastWeekShows(shows: List<MyShowsListItem.UpcomingShowViewModel>) {
        lastWeekSection.shows = shows
        recalculatePositions()
    }

    fun setUpcomingShows(shows: List<MyShowsListItem.UpcomingShowViewModel>) {
        upcomingSection.shows = shows
        recalculatePositions()
        onModelModified()
    }

    fun setTbaShows(shows: List<MyShowsListItem.ShowViewModel>) {
        tbaSection.shows = shows
        recalculatePositions()
        onModelModified()
    }

    fun setEndedShows(shows: List<MyShowsListItem.ShowViewModel>) {
        endedSection.shows = shows
        recalculatePositions()
        onModelModified()
    }

    fun setArchivedShows(shows: List<MyShowsListItem.ShowViewModel>) {
        archivedSection.shows = shows
        recalculatePositions()
        onModelModified()
    }

    fun count(): Int {
        return lastWeekSection.shows.size +
                upcomingSection.shows.size +
                tbaSection.shows.size +
                endedSection.shows.size +
                archivedSection.shows.size
    }

    fun sectionAt(position: Int): Section {
        return when (position) {
            in lastWeekSection.positions -> lastWeekSection
            in upcomingSection.positions -> upcomingSection
            in tbaSection.positions -> tbaSection
            in endedSection.positions -> endedSection
            in archivedSection.positions -> archivedSection
            else -> throw IllegalStateException("Can't find section for position $position")
        }
    }

    @IdRes
    fun viewType(position: Int): Int {
        val section = sectionAt(position)
        if (position == section.positions.first) return R.id.view_type_group

        return when (section.showAt(position)) {
            is MyShowsListItem.UpcomingShowViewModel -> R.id.view_type_upcoming_show
            is MyShowsListItem.ShowViewModel -> R.id.view_type_show
        }
    }

    private fun recalculatePositions() {
        lastWeekSection.positions = 0..lastWeekSection.shows.size

        val upcomingStart = lastWeekSection.positions.last + 1
        upcomingSection.positions = upcomingStart..(upcomingStart + upcomingSection.shows.size)

        val tbaStart = upcomingSection.positions.last + 1
        tbaSection.positions = tbaStart..(tbaStart + tbaSection.shows.size)

        val endedStart = tbaSection.positions.last + 1
        endedSection.positions = endedStart..(endedStart + endedSection.shows.size)

        val archivedStart = endedSection.positions.last + 1
        archivedSection.positions = archivedStart..(archivedStart + archivedSection.shows.size)
    }

    class Section(@StringRes val nameRes: Int, var isExpanded: Boolean) {
        var shows: List<MyShowsListItem> = emptyList()
        var positions: IntRange = IntRange.EMPTY

        fun showAt(position: Int): MyShowsListItem {
            return shows[position - positions.first - 1]
        }
    }
}
