package dev.polek.episodetracker.showdetails.episodes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.MergeAdapter
import dev.polek.episodetracker.common.presentation.showdetails.model.SeasonViewModel
import dev.polek.episodetracker.databinding.EpisodesFragmentBinding

class EpisodesFragment : Fragment() {

    private var binding: EpisodesFragmentBinding? = null
    private var adapter: MergeAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View?
    {
        val binding = EpisodesFragmentBinding.inflate(inflater, container, false)
        this.binding = binding

        binding.recyclerView.adapter = adapter

        return binding.root
    }

    fun displayEpisodes(seasons: List<SeasonViewModel>) {
        adapter = MergeAdapter(seasons.map(::SeasonAdapter))
        binding?.recyclerView?.adapter = adapter
    }

    fun hideRefreshProgress() {
        // TODO("not implemented")
    }

    fun setBottomPadding(padding: Int) {
        // TODO("not implemented")
    }

    companion object {
        fun instance() = EpisodesFragment()
    }
}
