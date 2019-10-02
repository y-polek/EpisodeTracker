package dev.polek.episodetracker.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dev.polek.episodetracker.databinding.DiscoverFragmentBinding

class DiscoverFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View?
    {
        val binding = DiscoverFragmentBinding.inflate(inflater)
        return binding.root
    }

    companion object {
        fun instance() = DiscoverFragment()
    }
}
