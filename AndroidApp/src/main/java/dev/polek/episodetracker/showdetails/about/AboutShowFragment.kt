package dev.polek.episodetracker.showdetails.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dev.polek.episodetracker.common.presentation.showdetails.model.ShowDetailsViewModel
import dev.polek.episodetracker.common.presentation.showdetails.model.TrailerViewModel
import dev.polek.episodetracker.databinding.AboutShowFragmentBinding

class AboutShowFragment : Fragment() {

    private val genreAdapter = GenreAdapter()
    private val trailerAdapter = TrailerAdapter(onPlayClicked = { trailer ->

    })

    private var binding: AboutShowFragmentBinding? = null
    private var show: ShowDetailsViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View?
    {
        val binding = AboutShowFragmentBinding.inflate(inflater, container, false)
        this.binding = binding

        binding.genresRecyclerView.adapter = genreAdapter
        binding.trailersRecyclerView.adapter = trailerAdapter

        bindShow()

        return binding.root
    }

    fun displayShowDetails(show: ShowDetailsViewModel) {
        this.show = show
        bindShow()
    }

    fun displayTrailers(trailers: List<TrailerViewModel>) {
        trailerAdapter.trailers = trailers
    }

    private fun bindShow() {
        val binding = this.binding ?: return
        val show = this.show ?: return

        genreAdapter.genres = show.genres
        binding.overview.text = show.overview
    }

    companion object {
        fun instance() = AboutShowFragment()
    }
}
