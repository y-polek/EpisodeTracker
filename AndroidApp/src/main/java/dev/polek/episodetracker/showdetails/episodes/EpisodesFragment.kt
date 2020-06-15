package dev.polek.episodetracker.showdetails.episodes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.MergeAdapter
import dev.polek.episodetracker.common.presentation.showdetails.model.EpisodeViewModel
import dev.polek.episodetracker.common.presentation.showdetails.model.SeasonViewModel
import dev.polek.episodetracker.databinding.EpisodesFragmentBinding
import dev.polek.episodetracker.utils.doOnClick
import dev.polek.episodetracker.utils.setBottomPadding

class EpisodesFragment : Fragment(), SeasonAdapter.Listener {

    private var binding: EpisodesFragmentBinding? = null
    private var adapter: MergeAdapter? = null

    private var bottomPadding: Int? = null
    private var progressVisible: Boolean = false
    private var errorVisible: Boolean = false

    private val listener: Listener?
        get() = activity as? Listener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View?
    {
        val binding = EpisodesFragmentBinding.inflate(inflater, container, false)
        this.binding = binding

        binding.recyclerView.adapter = adapter

        binding.swipeRefresh.setOnRefreshListener {
            listener?.onEpisodesSwipeRefresh()
        }

        binding.retryButton.doOnClick {
            listener?.onEpisodesRetryButtonClicked()
        }

        bottomPadding?.let(::setBottomPadding)
        if (progressVisible) showProgress() else hideProgress()
        if (errorVisible) showError() else hideError()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onSeasonWatchedStateToggled(season: SeasonViewModel) {
        listener?.onSeasonWatchedStateToggled(season)
    }

    override fun onEpisodeWatchedStateToggled(episode: EpisodeViewModel) {
        listener?.onEpisodeWatchedStateToggled(episode)
    }

    fun displayEpisodes(seasons: List<SeasonViewModel>) {
        adapter = MergeAdapter(seasons.map { SeasonAdapter(it, this) })
        binding?.recyclerView?.adapter = adapter
    }

    fun showProgress() {
        progressVisible = true
        binding?.swipeRefresh?.isRefreshing = true
    }

    fun hideProgress() {
        progressVisible = false
        binding?.swipeRefresh?.isRefreshing = false
    }

    fun showError() {
        errorVisible = true
        binding?.errorView?.isVisible = true
    }

    fun hideError() {
        errorVisible = false
        binding?.errorView?.isVisible = false
    }

    fun hideRefreshProgress() {
        binding?.swipeRefresh?.isRefreshing = false
    }

    fun setBottomPadding(padding: Int) {
        bottomPadding = padding
        binding?.recyclerView?.setBottomPadding(padding)
    }

    fun reloadSeason(number: Int) {
        findAdapterForSeason(number)?.reload()
    }

    private fun findAdapterForSeason(number: Int): SeasonAdapter? {
        return adapter?.adapters?.firstOrNull { adapter ->
            (adapter as SeasonAdapter).season.number == number
        } as? SeasonAdapter
    }

    companion object {
        fun instance() = EpisodesFragment()
    }

    interface Listener {
        fun onSeasonWatchedStateToggled(season: SeasonViewModel)
        fun onEpisodeWatchedStateToggled(episode: EpisodeViewModel)
        fun onEpisodesSwipeRefresh()
        fun onEpisodesRetryButtonClicked()
    }
}
