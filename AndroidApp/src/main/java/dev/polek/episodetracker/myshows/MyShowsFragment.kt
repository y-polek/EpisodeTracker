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
