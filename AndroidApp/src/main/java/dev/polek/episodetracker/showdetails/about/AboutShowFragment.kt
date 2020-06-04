package dev.polek.episodetracker.showdetails.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import dev.polek.episodetracker.common.presentation.showdetails.model.CastMemberViewModel
import dev.polek.episodetracker.common.presentation.showdetails.model.RecommendationViewModel
import dev.polek.episodetracker.common.presentation.showdetails.model.ShowDetailsViewModel
import dev.polek.episodetracker.common.presentation.showdetails.model.TrailerViewModel
import dev.polek.episodetracker.databinding.AboutShowFragmentBinding

class AboutShowFragment : Fragment() {

    private val genreAdapter = GenreAdapter()
    private val trailerAdapter = TrailerAdapter(onPlayClicked = { trailer ->
        openUrl(trailer.url)
    })
    private val castAdapter = CastAdapter(onClicked = { castMember ->
        openUrl(castMember.wikipediaUrl)
    })
    private val recommendationAdapter = RecommendationAdapter(
        onClicked = { show ->
            listener?.onRecommendationClicked(show)
        },
        onAddButtonClicked = { show ->
            listener?.onAddRecommendationClicked(show)
        },
        onRemoveButtonClicked = { show ->
            listener?.onRemoveRecommendationClicked(show)
        }
    )

    private var binding: AboutShowFragmentBinding? = null
    private var show: ShowDetailsViewModel? = null

    private val listener: Listener?
        get() = activity as? Listener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View?
    {
        val binding = AboutShowFragmentBinding.inflate(inflater, container, false)
        this.binding = binding

        binding.genresRecyclerView.adapter = genreAdapter
        binding.trailersRecyclerView.adapter = trailerAdapter
        binding.castRecyclerView.adapter = castAdapter
        binding.recommendationsRecyclerView.adapter = recommendationAdapter

        binding.trailersLayout.isVisible = trailerAdapter.itemCount > 0
        binding.castLayout.isVisible = castAdapter.itemCount > 0
        binding.recommendationsLayout.isVisible = recommendationAdapter.itemCount > 0

        bindShow()

        return binding.root
    }

    fun displayShowDetails(show: ShowDetailsViewModel) {
        this.show = show
        bindShow()
    }

    fun displayTrailers(trailers: List<TrailerViewModel>) {
        trailerAdapter.trailers = trailers
        binding?.trailersLayout?.isVisible = trailers.isNotEmpty()
    }

    fun displayCast(cast: List<CastMemberViewModel>) {
        castAdapter.cast = cast
        binding?.castLayout?.isVisible = cast.isNotEmpty()
    }

    fun displayRecommendations(recommendations: List<RecommendationViewModel>) {
        recommendationAdapter.setRecommendations(recommendations)
        binding?.recommendationsLayout?.isVisible = recommendations.isNotEmpty()
    }

    fun updateRecommendation(recommendation: RecommendationViewModel) {
        recommendationAdapter.updateRecommendation(recommendation)
    }

    private fun bindShow() {
        val binding = this.binding ?: return
        val show = this.show ?: return

        genreAdapter.genres = show.genres
        binding.overview.text = show.overview
    }

    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        val pm = activity?.packageManager ?: return
        if (intent.resolveActivity(pm) != null) {
            startActivity(intent)
        }
    }

    companion object {
        fun instance() = AboutShowFragment()
    }

    interface Listener {
        fun onRecommendationClicked(show: RecommendationViewModel)
        fun onAddRecommendationClicked(show: RecommendationViewModel)
        fun onRemoveRecommendationClicked(show: RecommendationViewModel)
    }
}
