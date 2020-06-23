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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dev.polek.episodetracker.App
import dev.polek.episodetracker.R
import dev.polek.episodetracker.common.presentation.discover.DiscoverView
import dev.polek.episodetracker.common.presentation.discover.model.DiscoverResultViewModel
import dev.polek.episodetracker.databinding.DiscoverFragmentBinding
import dev.polek.episodetracker.showdetails.ShowDetailsActivity
import dev.polek.episodetracker.utils.doOnClick
import dev.polek.episodetracker.utils.recyclerview.HideKeyboardScrollListener
import dev.polek.episodetracker.utils.showKeyboard

class DiscoverFragment : Fragment(), DiscoverView, DiscoverAdapter.Listener {

    private val presenter = App.instance.di.discoverPresenter()

    private var _binding: DiscoverFragmentBinding? = null
    private val binding get() = _binding!!
    private val adapter = DiscoverAdapter()

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
        _binding = DiscoverFragmentBinding.inflate(inflater)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            addOnScrollListener(HideKeyboardScrollListener)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            setHasFixedSize(true)
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

    fun focusSearch() {
        binding.searchView.requestFocus()
        context?.showKeyboard(binding.searchView)
    }

    fun scrollToTop() {
        binding.recyclerView.smoothScrollToPosition(0)
    }

    ///////////////////////////////////////////////////////////////////////////
    // region DiscoverView implementation

    override fun onResultClicked(result: DiscoverResultViewModel) {
        presenter.onShowClicked(result)
    }

    override fun onAddButtonClicked(result: DiscoverResultViewModel) {
        presenter.onAddButtonClicked(result)
    }

    override fun onRemoveButtonClicked(result: DiscoverResultViewModel) {
        presenter.onRemoveButtonClicked(result)
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
        val previousResults = adapter.getResults()
        adapter.setResults(results)
        if (previousResults.isNotEmpty() && !results.isEqualTo(previousResults)) {
            binding.recyclerView.scrollToPosition(0)
        }
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
        MaterialAlertDialogBuilder(context)
            .setTitle(getString(R.string.remove_show_confirmation, result.name))
            .setPositiveButton(R.string.action_remove) { _, _ -> callback(true) }
            .setNegativeButton(R.string.action_cancel) { _, _ -> callback(false) }
            .show()
    }

    override fun openDiscoverShow(show: DiscoverResultViewModel) {
        startActivity(ShowDetailsActivity.intent(requireContext(), show.id, show.name))
    }
    //endregion
    ///////////////////////////////////////////////////////////////////////////

    companion object {

        fun instance() = DiscoverFragment()

        private fun List<DiscoverResultViewModel>.isEqualTo(list: List<DiscoverResultViewModel>): Boolean {
            if (this.size != list.size) return false
            for (i in this.indices) {
                if (this[i].id != list[i].id) return false
            }
            return true
        }
    }
}
