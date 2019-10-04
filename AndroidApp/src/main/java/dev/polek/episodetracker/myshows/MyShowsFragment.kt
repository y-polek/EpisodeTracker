package dev.polek.episodetracker.myshows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
import com.google.android.material.appbar.AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
import dev.polek.episodetracker.R
import dev.polek.episodetracker.databinding.MyShowsFragmentBinding
import dev.polek.episodetracker.myshows.model.MyShowsListItem.ShowViewModel
import dev.polek.episodetracker.myshows.model.MyShowsListItem.UpcomingShowViewModel
import dev.polek.episodetracker.myshows.model.MyShowsViewModel
import dev.polek.episodetracker.utils.HideKeyboardScrollListener
import dev.polek.episodetracker.utils.scrollFlags
import dev.polek.episodetracker.utils.setTopMargin
import dev.polek.episodetracker.utils.setTopPadding

class MyShowsFragment : Fragment() {

    private lateinit var binding: MyShowsFragmentBinding

    private val showsAdapter = MyShowsAdapter(onGroupVisibilityChanged = {
        makeSearchBarVisible()
    })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View?
    {
        binding = MyShowsFragmentBinding.inflate(inflater)

        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = showsAdapter
            addOnScrollListener(HideKeyboardScrollListener())
        }

        binding.appBarLayout.outlineProvider = null

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(query: String): Boolean {
                setSearchBarBehaviour(showAlways = query.isNotBlank())
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchView.clearFocus()
                return true
            }
        })

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val searchBarMarginTop = resources.getDimensionPixelOffset(R.dimen.search_bar_margin_top)
            val searchBarMarginBottom = resources.getDimensionPixelOffset(R.dimen.search_bar_margin_bottom)
            binding.searchBar.setTopMargin(searchBarMarginTop + insets.systemWindowInsetTop)

            val recyclerViewTopPadding =
                searchBarMarginTop + searchBarMarginBottom + binding.searchBar.height + insets.systemWindowInsetTop
            if (binding.recyclerView.paddingTop != recyclerViewTopPadding) {
                binding.recyclerView.setTopPadding(recyclerViewTopPadding)
                binding.recyclerView.scrollToPosition(0)
            }

            return@setOnApplyWindowInsetsListener insets
        }




        val model = MyShowsViewModel(
            upcomingShows = listOf(
                UpcomingShowViewModel(
                    name = "Star Trek: Discovery",
                    backdropUrl = "https://image.tmdb.org/t/p/w1066_and_h600_bestv2/rhE2h8WYJOPuBlMl20MQRnJw3aq.jpg",
                    episodeNumber = "S01 E01",
                    episodeName = "The Vulcan Hello",
                    timeLeft = "4 days"),
                UpcomingShowViewModel(
                    name = "South Park",
                    backdropUrl = "https://image.tmdb.org/t/p/w1066_and_h600_bestv2/mSDKNVvDfitFE6Fb6fSSl5DQmgS.jpg",
                    episodeNumber = "S23 E01",
                    episodeName = "TBA",
                    timeLeft = "1 month")),
            toBeAnnouncedShows = listOf(
                ShowViewModel(
                    name = "The Big Bang Theory",
                    backdropUrl = "https://image.tmdb.org/t/p/w1066_and_h600_bestv2/nGsNruW3W27V6r4gkyc3iiEGsKR.jpg")),
            endedShows = listOf(
                ShowViewModel(
                    name = "Game of Thrones",
                    backdropUrl = "https://image.tmdb.org/t/p/w1066_and_h600_bestv2/c0Qt5uorF3WHv9pMKhV5uprNyVl.jpg"),
                ShowViewModel(
                    name = "Breaking Bad",
                    backdropUrl = "https://image.tmdb.org/t/p/w1066_and_h600_bestv2/eSzpy96DwBujGFj0xMbXBcGcfxX.jpg")),
            isUpcomingExpanded = true,
            isToBeAnnouncedExpanded = true,
            isEndedExpanded = true
        )

        showsAdapter.viewModel = model



        return binding.root
    }

    private fun setSearchBarBehaviour(showAlways: Boolean) {
        binding.searchBar.scrollFlags = if (showAlways) 0 else SCROLL_FLAG_SCROLL or SCROLL_FLAG_ENTER_ALWAYS
    }

    private fun makeSearchBarVisible() {
        val scrollFlags = binding.searchBar.scrollFlags
        binding.searchBar.scrollFlags = 0
        binding.root.post {
            binding.searchBar.scrollFlags = scrollFlags
        }
    }

    companion object {
        fun instance() = MyShowsFragment()
    }
}
