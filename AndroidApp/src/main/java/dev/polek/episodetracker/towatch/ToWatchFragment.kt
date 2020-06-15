package dev.polek.episodetracker.towatch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import dev.polek.episodetracker.App
import dev.polek.episodetracker.common.model.EpisodeNumber
import dev.polek.episodetracker.common.presentation.towatch.ToWatchShowViewModel
import dev.polek.episodetracker.common.presentation.towatch.ToWatchView
import dev.polek.episodetracker.databinding.ToWatchFragmentBinding
import dev.polek.episodetracker.showdetails.ShowDetailsActivity
import dev.polek.episodetracker.utils.doOnClick
import dev.polek.episodetracker.utils.recyclerview.CloseSwipeActionsScrollListener
import dev.polek.episodetracker.utils.recyclerview.HideKeyboardScrollListener

class ToWatchFragment : Fragment(), ToWatchView, ToWatchAdapter.Listener {

    private val presenter = App.instance.di.toWatchPresenter()

    private var _binding: ToWatchFragmentBinding? = null
    private val binding get() = _binding!!
    private val adapter = ToWatchAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter.listener = this
    }

    override fun onDestroy() {
        adapter.listener = null
        super.onDestroy()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View?
    {
        _binding = ToWatchFragmentBinding.inflate(inflater)

        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            addOnScrollListener(HideKeyboardScrollListener)
            addOnScrollListener(CloseSwipeActionsScrollListener)
            setHasFixedSize(true)
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                binding.searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                presenter.onSearchQueryChanged(query)
                return true
            }
        })

        binding.showAllButton.doOnClick {
            binding.searchView.setQuery("", true)
            binding.searchView.clearFocus()
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

    override fun onShowClicked(show: ToWatchShowViewModel) {
        presenter.onShowClicked(show)
    }

    override fun onCheckButtonClicked(show: ToWatchShowViewModel) {
        presenter.onWatchedButtonClicked(show)
    }

    override fun onArchiveButtonClicked(show: ToWatchShowViewModel) {
        presenter.onArchiveShowClicked(show)
    }

    override fun onMarkAllWatchedButtonClicked(show: ToWatchShowViewModel) {
        presenter.onMarkAllWatchedClicked(show)
    }

    ///////////////////////////////////////////////////////////////////////////
    // region ToWatchView implementation

    override fun displayShows(shows: List<ToWatchShowViewModel>) {
        adapter.shows = shows
    }

    override fun showEmptyMessage(isFiltered: Boolean) {
        binding.emptySearchView.isVisible = isFiltered
        binding.emptyView.isVisible = !isFiltered
    }

    override fun hideEmptyMessage() {
        binding.emptyView.isVisible = false
        binding.emptySearchView.isVisible = false
    }

    override fun openToWatchShowDetails(show: ToWatchShowViewModel, episode: EpisodeNumber) {
        startActivity(ShowDetailsActivity.intent(requireContext(), show.id, show.name))
    }
    // endregion
    ///////////////////////////////////////////////////////////////////////////

    companion object {
        fun instance() = ToWatchFragment()
    }
}
