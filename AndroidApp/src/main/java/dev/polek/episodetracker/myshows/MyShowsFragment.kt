package dev.polek.episodetracker.myshows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import dev.polek.episodetracker.App
import dev.polek.episodetracker.common.presentation.myshows.MyShowsView
import dev.polek.episodetracker.common.presentation.myshows.model.MyShowsListItem
import dev.polek.episodetracker.databinding.MyShowsFragmentBinding
import dev.polek.episodetracker.utils.HideKeyboardScrollListener

class MyShowsFragment : Fragment(), MyShowsView {

    private val presenter = App.instance.di.myShowsPresenter()

    private lateinit var binding: MyShowsFragmentBinding
    private val adapter = MyShowsAdapter()

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

    ///////////////////////////////////////////////////////////////////////////
    //region MyShowsView implementation

    override fun displayLastWeekShows(shows: List<MyShowsListItem.UpcomingShowViewModel>, isVisible: Boolean) {
        // TODO("not implemented")
    }

    override fun displayUpcomingShows(shows: List<MyShowsListItem.UpcomingShowViewModel>) {
        // TODO("not implemented")
    }

    override fun displayToBeAnnouncedShows(shows: List<MyShowsListItem.ShowViewModel>) {
        // TODO("not implemented")
    }

    override fun displayEndedShows(shows: List<MyShowsListItem.ShowViewModel>) {
        // TODO("not implemented")
    }

    override fun displayArchivedShows(shows: List<MyShowsListItem.ShowViewModel>) {
        // TODO("not implemented")
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
        fun instance() = MyShowsFragment()
    }
}
