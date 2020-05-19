package dev.polek.episodetracker.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dev.polek.episodetracker.App
import dev.polek.episodetracker.common.presentation.discover.DiscoverView
import dev.polek.episodetracker.common.presentation.discover.model.DiscoverResultViewModel
import dev.polek.episodetracker.databinding.DiscoverFragmentBinding
import dev.polek.episodetracker.utils.HideKeyboardScrollListener
import dev.polek.episodetracker.utils.doOnClick

class DiscoverFragment : Fragment(), DiscoverView {

    private lateinit var binding: DiscoverFragmentBinding
    private val adapter = DiscoverAdapter()

    private val presenter = App.instance.di.discoverPresenter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View?
    {
        binding = DiscoverFragmentBinding.inflate(inflater)

        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            addOnScrollListener(HideKeyboardScrollListener())
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        binding.recyclerView.adapter = adapter

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                binding.searchView.clearFocus()
                presenter.onSearchQuerySubmitted(query.trim())
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                return true
            }
        })

        binding.swipeRefresh.isEnabled = false

        binding.retryButton.doOnClick {
            presenter.onRetryButtonClicked()
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

    override fun showPrompt() {
        binding.promptView.isVisible = true
    }

    override fun hidePrompt() {
        binding.promptView.isVisible = false
    }

    override fun showProgress() {
        binding.swipeRefresh.isRefreshing = true
    }

    override fun hideProgress() {
        binding.swipeRefresh.isRefreshing = false
    }

    override fun showSearchResults(results: List<DiscoverResultViewModel>) {
        adapter.setResults(results)
    }

    override fun updateSearchResult(result: DiscoverResultViewModel) {
        adapter.updateResult(result)
    }

    override fun updateSearchResults() {
        adapter.notifyDataSetChanged()
    }

    override fun showEmptyMessage() {
        binding.emptyView.isVisible = true
    }

    override fun hideEmptyMessage() {
        binding.emptyView.isVisible = false
    }

    override fun showError() {
        binding.errorView.isVisible = true
    }

    override fun hideError() {
        binding.errorView.isVisible = false
    }

    override fun displayRemoveShowConfirmation(result: DiscoverResultViewModel, callback: (confirmed: Boolean) -> Unit) {

    }

    override fun openDiscoverShow(show: DiscoverResultViewModel) {

    }

    companion object {
        fun instance() = DiscoverFragment()
    }
}
