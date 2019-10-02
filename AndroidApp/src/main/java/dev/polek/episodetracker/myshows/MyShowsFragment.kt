package dev.polek.episodetracker.myshows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import dev.polek.episodetracker.databinding.MyShowsFragmentBinding

class MyShowsFragment : Fragment() {

    private val showsAdapter = MyShowsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View?
    {
        val binding = MyShowsFragmentBinding.inflate(inflater).apply {
            recyclerView.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext())
                adapter = showsAdapter
            }

            appBarLayout.apply {
                outlineProvider = null
            }
        }

        return binding.root
    }

    companion object {
        fun instance() = MyShowsFragment()
    }
}
