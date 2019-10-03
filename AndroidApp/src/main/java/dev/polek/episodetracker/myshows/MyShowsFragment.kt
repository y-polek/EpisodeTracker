package dev.polek.episodetracker.myshows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
import com.google.android.material.appbar.AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
import dev.polek.episodetracker.databinding.MyShowsFragmentBinding
import dev.polek.episodetracker.myshows.model.MyShowsListItem.ShowViewModel
import dev.polek.episodetracker.myshows.model.MyShowsListItem.UpcomingShowViewModel
import dev.polek.episodetracker.myshows.model.MyShowsViewModel
import dev.polek.episodetracker.utils.HideKeyboardScrollListener
import dev.polek.episodetracker.utils.dp2px
import dev.polek.episodetracker.utils.setTopMargin
import dev.polek.episodetracker.utils.setTopPadding

class MyShowsFragment : Fragment() {

    private val showsAdapter = MyShowsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View?
    {
        val binding: MyShowsFragmentBinding = MyShowsFragmentBinding.inflate(inflater)

        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = showsAdapter
            addOnScrollListener(HideKeyboardScrollListener())
        }

        binding.appBarLayout.outlineProvider = null

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(query: String): Boolean {
                setSearchViewBehaviour(binding, showAlways = query.isNotBlank())
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchView.clearFocus()
                return true
            }
        })

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val searchBarVerticalMargin = requireContext().dp2px(8f)
            binding.searchBar.setTopMargin(searchBarVerticalMargin + insets.systemWindowInsetTop)

            val recyclerViewTopPadding =
                2 * searchBarVerticalMargin + binding.searchBar.height + insets.systemWindowInsetTop
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
                    episodeNumber = "S01E01",
                    episodeName = "The Vulcan Hello",
                    timeLeft = "4 days"),
                UpcomingShowViewModel(
                    name = "South Park",
                    backdropUrl = "https://image.tmdb.org/t/p/w1066_and_h600_bestv2/mSDKNVvDfitFE6Fb6fSSl5DQmgS.jpg",
                    episodeNumber = "S23E01",
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
            isEndedExpanded = true,
            isToBeAnnouncedExpanded = true)

        showsAdapter.items = model.items



        return binding.root
    }

    private fun setSearchViewBehaviour(binding: MyShowsFragmentBinding, showAlways: Boolean) {
        val layoutParams = binding.searchBar.layoutParams as AppBarLayout.LayoutParams
        layoutParams.scrollFlags = if (showAlways) 0 else SCROLL_FLAG_SCROLL or SCROLL_FLAG_ENTER_ALWAYS
    }

    companion object {
        fun instance() = MyShowsFragment()
    }
}
