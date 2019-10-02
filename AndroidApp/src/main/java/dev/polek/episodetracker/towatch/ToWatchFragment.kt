package dev.polek.episodetracker.towatch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dev.polek.episodetracker.databinding.ToWatchFragmentBinding

class ToWatchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View?
    {
        val binding = ToWatchFragmentBinding.inflate(inflater)
        return binding.root
    }

    companion object {
        fun instance() = ToWatchFragment()
    }
}
