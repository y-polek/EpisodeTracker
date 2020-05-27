package dev.polek.episodetracker.myshows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.MergeAdapter
import dev.polek.episodetracker.App
import dev.polek.episodetracker.R
import dev.polek.episodetracker.common.presentation.myshows.MyShowsView
import dev.polek.episodetracker.common.presentation.myshows.model.MyShowsListItem
import dev.polek.episodetracker.databinding.MyShowsFragmentBinding
import dev.polek.episodetracker.utils.HideKeyboardScrollListener

class MyShowsFragment : Fragment(), MyShowsView, MyShowsAdapterListener {

    private val presenter = App.instance.di.myShowsPresenter()

    private lateinit var binding: MyShowsFragmentBinding
    private val lastWeekAdapter = UpcomingShowsAdapter(R.string.my_shows_last_week, presenter.isLastWeekExpanded)
    private val upcomingAdapter = UpcomingShowsAdapter(R.string.my_shows_upcoming, presenter.isUpcomingExpanded)
    private val tbaAdapter = ShowsAdapter(R.string.my_shows_tba, presenter.isTbaExpanded)
    private val endedAdapter = ShowsAdapter(R.string.my_shows_ended, presenter.isEndedExpanded)
    private val archivedAdapter = ShowsAdapter(R.string.my_shows_archived, presenter.isArchivedExpanded)
    private val adapter = MergeAdapter(adapterConfig, lastWeekAdapter, upcomingAdapter, tbaAdapter, endedAdapter, archivedAdapter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lastWeekAdapter.listener = this
        upcomingAdapter.listener = this
        tbaAdapter.listener = this
        endedAdapter.listener = this
        archivedAdapter.listener = this
    }

    override fun onDestroy() {
        lastWeekAdapter.listener = null
        upcomingAdapter.listener = null
        tbaAdapter.listener = null
        endedAdapter.listener = null
        archivedAdapter.listener = null
        super.onDestroy()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View?
    {
        binding = MyShowsFragmentBinding.inflate(inflater)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            addOnScrollListener(HideKeyboardScrollListener())
            setHasFixedSize(true)
        }
        binding.recyclerView.adapter = adapter

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                presenter.onSearchQueryChanged(query)
                return true
            }
        })

        binding.swipeRefresh.setOnRefreshListener {
            presenter.onRefreshRequested()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this)
    }

    override fun onDestroyView() {
        presenter.detachView()
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        presenter.onViewAppeared()
    }

    override fun onPause() {
        presenter.onViewDisappeared()
        super.onPause()
    }

    override fun onShowClicked(show: MyShowsListItem.ShowViewModel) {
        presenter.onShowClicked(show)
    }

    ///////////////////////////////////////////////////////////////////////////
    //region MyShowsView implementation

    override fun displayLastWeekShows(shows: List<MyShowsListItem.UpcomingShowViewModel>, isVisible: Boolean) {
        lastWeekAdapter.shows = shows
    }

    override fun displayUpcomingShows(shows: List<MyShowsListItem.UpcomingShowViewModel>) {
        upcomingAdapter.shows = shows
    }

    override fun displayToBeAnnouncedShows(shows: List<MyShowsListItem.ShowViewModel>) {
        tbaAdapter.shows = shows
    }

    override fun displayEndedShows(shows: List<MyShowsListItem.ShowViewModel>) {
        endedAdapter.shows = shows
    }

    override fun displayArchivedShows(shows: List<MyShowsListItem.ShowViewModel>) {
        archivedAdapter.shows = shows
    }

    override fun showEmptyMessage(isFiltered: Boolean) {
        // TODO("not implemented")
    }

    override fun hideEmptyMessage() {
        // TODO("not implemented")
    }

    override fun hideRefresh() {
        binding.swipeRefresh.isRefreshing = false
    }

    override fun openMyShowDetails(show: MyShowsListItem.ShowViewModel) {
        // TODO("not implemented")
    }
    //endregion
    ///////////////////////////////////////////////////////////////////////////

    companion object {
        private val adapterConfig = MergeAdapter.Config.Builder()
            .setIsolateViewTypes(false)
            .build()

        fun instance() = MyShowsFragment()
    }
}
