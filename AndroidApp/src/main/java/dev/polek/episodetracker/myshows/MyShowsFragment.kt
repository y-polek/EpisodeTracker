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
import com.google.android.material.appbar.AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
import com.google.android.material.appbar.AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dev.polek.episodetracker.App
import dev.polek.episodetracker.MainActivity
import dev.polek.episodetracker.R
import dev.polek.episodetracker.common.presentation.myshows.MyShowsView
import dev.polek.episodetracker.common.presentation.myshows.model.MyShowsListItem
import dev.polek.episodetracker.databinding.MyShowsFragmentBinding
import dev.polek.episodetracker.showdetails.ShowDetailsActivity
import dev.polek.episodetracker.utils.doOnClick
import dev.polek.episodetracker.utils.recyclerview.CloseSwipeActionsScrollListener
import dev.polek.episodetracker.utils.recyclerview.HideKeyboardScrollListener
import dev.polek.episodetracker.utils.scrollFlags

class MyShowsFragment : Fragment(), MyShowsView {

    private val presenter = App.instance.di.myShowsPresenter()

    private var _binding: MyShowsFragmentBinding? = null
    private val binding get() = _binding!!

    private val lastWeekAdapter = MyShowsAdapter(
        R.string.my_shows_last_week,
        presenter.isLastWeekExpanded,
        onShowClicked = ::onShowClicked,
        onRemoveButtonClicked = presenter::onRemoveShowClicked,
        onArchiveButtonClicked = presenter::onArchiveShowClicked,
        onUnarchiveButtonClicked = presenter::onUnarchiveShowClicked,
        onExpandStateChanged = { isExpanded ->
            presenter.isLastWeekExpanded = isExpanded
        })

    private val upcomingAdapter = MyShowsAdapter(
        R.string.my_shows_upcoming,
        presenter.isUpcomingExpanded,
        onShowClicked = ::onShowClicked,
        onRemoveButtonClicked = presenter::onRemoveShowClicked,
        onArchiveButtonClicked = presenter::onArchiveShowClicked,
        onUnarchiveButtonClicked = presenter::onUnarchiveShowClicked,
        onExpandStateChanged = { isExpanded ->
            presenter.isUpcomingExpanded = isExpanded
        })

    private val tbaAdapter = MyShowsAdapter(
        R.string.my_shows_tba,
        presenter.isTbaExpanded,
        onShowClicked = ::onShowClicked,
        onRemoveButtonClicked = presenter::onRemoveShowClicked,
        onArchiveButtonClicked = presenter::onArchiveShowClicked,
        onUnarchiveButtonClicked = presenter::onUnarchiveShowClicked,
        onExpandStateChanged = { isExpanded ->
            presenter.isTbaExpanded = isExpanded
        })

    private val endedAdapter = MyShowsAdapter(
        R.string.my_shows_ended,
        presenter.isEndedExpanded,
        onShowClicked = ::onShowClicked,
        onRemoveButtonClicked = presenter::onRemoveShowClicked,
        onArchiveButtonClicked = presenter::onArchiveShowClicked,
        onUnarchiveButtonClicked = presenter::onUnarchiveShowClicked,
        onExpandStateChanged = { isExpanded ->
            presenter.isEndedExpanded = isExpanded
        })

    private val archivedAdapter = MyShowsAdapter(
        R.string.my_shows_archived,
        presenter.isArchivedExpanded,
        onShowClicked = ::onShowClicked,
        onRemoveButtonClicked = presenter::onRemoveShowClicked,
        onArchiveButtonClicked = presenter::onArchiveShowClicked,
        onUnarchiveButtonClicked = presenter::onUnarchiveShowClicked,
        onExpandStateChanged = { isExpanded ->
            presenter.isArchivedExpanded = isExpanded
        })

    private val adapter = MergeAdapter(lastWeekAdapter, upcomingAdapter, tbaAdapter, endedAdapter, archivedAdapter)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View?
    {
        _binding = MyShowsFragmentBinding.inflate(inflater)

        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            addOnScrollListener(HideKeyboardScrollListener)
            addOnScrollListener(CloseSwipeActionsScrollListener)
            setHasFixedSize(true)
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                presenter.onSearchQueryChanged(query)
                binding.searchBar.scrollFlags = if (query.trim().isEmpty()) {
                    SCROLL_FLAG_SCROLL or SCROLL_FLAG_ENTER_ALWAYS
                } else {
                    SCROLL_FLAG_ENTER_ALWAYS
                }
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
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        presenter.onViewAppeared()
    }

    override fun onPause() {
        presenter.onViewDisappeared()
        super.onPause()
    }

    fun scrollToTop() {
        binding.recyclerView.smoothScrollToPosition(0)
    }

    private fun onShowClicked(show: MyShowsListItem.ShowViewModel) {
        presenter.onShowClicked(show)

        adapter.adapters.forEach {
            (it as? CloseSwipeActionsScrollListener.SwipeActionsClosable)?.closeSwipeActions()
        }
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

    override fun displayRemoveShowConfirmation(
        show: MyShowsListItem.ShowViewModel,
        callback: (confirmed: Boolean) -> Unit)
    {
        MaterialAlertDialogBuilder(context)
            .setTitle(getString(R.string.remove_show_confirmation, show.name))
            .setPositiveButton(R.string.action_remove) { _, _ -> callback(true) }
            .setNegativeButton(R.string.action_cancel) { _, _ -> callback(false) }
            .show()
    }

    override fun openDiscoverScreen() {
        (activity as MainActivity).openDiscoverTab()
    }

    override fun openMyShowDetails(show: MyShowsListItem.ShowViewModel) {
        startActivity(ShowDetailsActivity.intent(context!!, show.id, show.name))
    }
    //endregion
    ///////////////////////////////////////////////////////////////////////////

    companion object {
        fun instance() = MyShowsFragment()
    }
}
