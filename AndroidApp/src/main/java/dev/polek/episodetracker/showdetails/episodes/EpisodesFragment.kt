package dev.polek.episodetracker.showdetails.episodes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.MergeAdapter
import dev.polek.episodetracker.common.presentation.showdetails.model.EpisodeViewModel
import dev.polek.episodetracker.common.presentation.showdetails.model.SeasonViewModel
import dev.polek.episodetracker.databinding.EpisodesFragmentBinding
import dev.polek.episodetracker.utils.setBottomPadding

class EpisodesFragment : Fragment(), SeasonAdapter.Listener {

    private var binding: EpisodesFragmentBinding? = null
    private var adapter: MergeAdapter? = null

    private var bottomPadding: Int? = null

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

        bottomPadding?.let(::setBottomPadding)

        return binding.root
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

    fun hideRefreshProgress() {
        // TODO("not implemented")
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
    }
}
