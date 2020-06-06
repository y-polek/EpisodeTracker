package dev.polek.episodetracker.showdetails.episodes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import dev.polek.episodetracker.databinding.EpisodesFragmentBinding

class EpisodesFragment : Fragment() {

    private lateinit var binding: EpisodesFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View?
    {
        binding = EpisodesFragmentBinding.inflate(inflater, container, false)

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = SeasonAdapter()

        return binding.root
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
