package dev.polek.episodetracker.showdetails.about

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import dev.polek.episodetracker.common.presentation.showdetails.model.CastMemberViewModel
import dev.polek.episodetracker.common.presentation.showdetails.model.RecommendationViewModel
import dev.polek.episodetracker.common.presentation.showdetails.model.ShowDetailsViewModel
import dev.polek.episodetracker.common.presentation.showdetails.model.TrailerViewModel
import dev.polek.episodetracker.common.utils.anyNotNull
import dev.polek.episodetracker.databinding.AboutShowFragmentBinding
import dev.polek.episodetracker.utils.doOnClick
import dev.polek.episodetracker.utils.setBottomPadding

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
    private var imdbRating: Float? = null
    private var bottomPadding: Int? = null

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

        binding.imdbButton.doOnClick {
            val imdbUrl = show?.imdbUrl ?: return@doOnClick
            openUrl(imdbUrl)
        }
        binding.homePageButton.doOnClick {
            show?.homePageUrl?.let(::openUrl)
        }
        binding.instagramButton.doOnClick {
            show?.instagramUrl?.let(::openUrl)
        }
        binding.facebookButton.doOnClick {
            show?.facebookUrl?.let(::openUrl)
        }
        binding.twitterButton.doOnClick {
            show?.twitterUrl?.let(::openUrl)
        }

        binding.swipeRefresh.setOnRefreshListener {
            listener?.onShowSwipeRefresh()
        }

        bindShow()
        bindImdbRating()
        bottomPadding?.let(::setBottomPadding)

        return binding.root
    }

    fun displayShowDetails(show: ShowDetailsViewModel) {
        this.show = show
        bindShow()
    }

    fun displayImdbRating(rating: Float) {
        imdbRating = rating
        bindImdbRating()
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

    fun hideRefreshProgress() {
        binding?.swipeRefresh?.isRefreshing = false
    }

    fun setBottomPadding(padding: Int) {
        bottomPadding = padding
        binding?.scrollView?.setBottomPadding(padding)
    }

    private fun bindShow() {
        val binding = this.binding ?: return
        val show = this.show ?: return

        genreAdapter.genres = show.genres

        binding.imdbButton.isVisible = show.imdbUrl != null
        binding.homePageButton.isVisible = show.homePageUrl != null
        binding.instagramButton.isVisible = show.instagramUrl != null
        binding.facebookButton.isVisible = show.facebookUrl != null
        binding.twitterButton.isVisible = show.twitterUrl != null
        binding.socialNetworksLayout.isVisible = anyNotNull(show.imdbUrl, show.homePageUrl, show.instagramUrl, show.facebookUrl, show.twitterUrl)

        binding.overview.text = show.overview
        binding.overview.isVisible = show.overview.isNotBlank()
    }

    @SuppressLint("SetTextI18n")
    private fun bindImdbRating() {
        val binding = this.binding ?: return
        val rating = this.imdbRating ?: return

        binding.imdbButton.text = "%.1f".format(rating)
    }

    private fun openUrl(url: String) {
        val intent = CustomTabsIntent.Builder()
            .addDefaultShareMenuItem()
            .build()
        intent.launchUrl(requireContext(), Uri.parse(url))
    }

    companion object {
        fun instance() = AboutShowFragment()
    }

    interface Listener {
        fun onRecommendationClicked(show: RecommendationViewModel)
        fun onAddRecommendationClicked(show: RecommendationViewModel)
        fun onRemoveRecommendationClicked(show: RecommendationViewModel)
        fun onShowSwipeRefresh()
    }
}
