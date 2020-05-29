package dev.polek.episodetracker.myshows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.MergeAdapter
import dev.polek.episodetracker.App
import dev.polek.episodetracker.MainActivity
import dev.polek.episodetracker.R
import dev.polek.episodetracker.common.presentation.myshows.MyShowsView
import dev.polek.episodetracker.common.presentation.myshows.model.MyShowsListItem
import dev.polek.episodetracker.databinding.MyShowsFragmentBinding
import dev.polek.episodetracker.utils.HideKeyboardScrollListener
import dev.polek.episodetracker.utils.doOnClick

class MyShowsFragment : Fragment(), MyShowsView {

    private val presenter = App.instance.di.myShowsPresenter()

    private lateinit var binding: MyShowsFragmentBinding

    private val lastWeekAdapter = UpcomingShowsAdapter(
        R.string.my_shows_last_week,
        presenter.isLastWeekExpanded,
        onShowClicked = ::onShowClicked,
        onExpandStateChanged = { isExpanded ->
            presenter.isLastWeekExpanded = isExpanded
        })

    private val upcomingAdapter = UpcomingShowsAdapter(
        R.string.my_shows_upcoming,
        presenter.isUpcomingExpanded,
        onShowClicked = ::onShowClicked,
        onExpandStateChanged = { isExpanded ->
            presenter.isUpcomingExpanded = isExpanded
        })

    private val tbaAdapter = ShowsAdapter(
        R.string.my_shows_tba,
        presenter.isTbaExpanded,
        onShowClicked = ::onShowClicked,
        onExpandStateChanged = { isExpanded ->
            presenter.isTbaExpanded = isExpanded
        })

    private val endedAdapter = ShowsAdapter(
        R.string.my_shows_ended,
        presenter.isEndedExpanded,
        onShowClicked = ::onShowClicked,
        onExpandStateChanged = { isExpanded ->
            presenter.isEndedExpanded = isExpanded
        })

    private val archivedAdapter = ShowsAdapter(
        R.string.my_shows_archived,
        presenter.isArchivedExpanded,
        onShowClicked = ::onShowClicked,
        onExpandStateChanged = { isExpanded ->
            presenter.isArchivedExpanded = isExpanded
        })

    private val adapter = MergeAdapter(lastWeekAdapter, upcomingAdapter, tbaAdapter, endedAdapter, archivedAdapter)

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

        binding.showAllButton.doOnClick {
            binding.searchView.setQuery("", true)
            binding.searchView.clearFocus()
        }

        binding.discoverButton.doOnClick {
            presenter.onDiscoverButtonClicked()
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

    private fun onShowClicked(show: MyShowsListItem.ShowViewModel) {
        presenter.onShowClicked(show)
    }

    ///////////////////////////////////////////////////////////////////////////
    //region MyShowsView implementation

    override fun displayLastWeekShows(shows: List<MyShowsListItem.UpcomingShowViewModel>, isVisible: Boolean) {
        lastWeekAdapter.shows = shows
        if (isVisible) {
            adapter.addAdapter(0, lastWeekAdapter)
        } else {
            adapter.removeAdapter(lastWeekAdapter)
        }
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
        binding.promptView.isVisible = !isFiltered
        binding.emptySearchView.isVisible = isFiltered
    }

    override fun hideEmptyMessage() {
        binding.promptView.isVisible = false
        binding.emptySearchView.isVisible = false
    }

    override fun hideRefresh() {
        binding.swipeRefresh.isRefreshing = false
    }

    override fun openDiscoverScreen() {
        (activity as MainActivity).openDiscoverTab()
    }

    override fun openMyShowDetails(show: MyShowsListItem.ShowViewModel) {
        // TODO("not implemented")
    }
    //endregion
    ///////////////////////////////////////////////////////////////////////////

    companion object {
        fun instance() = MyShowsFragment()
    }
}
