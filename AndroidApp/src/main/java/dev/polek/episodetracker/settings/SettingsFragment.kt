package dev.polek.episodetracker.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dev.polek.episodetracker.databinding.SettingsFragmentBinding

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View?
    {
        val binding = SettingsFragmentBinding.inflate(inflater)
        return binding.root
    }

    companion object {
        fun instance() = SettingsFragment()
    }
}
