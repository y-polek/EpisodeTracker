package dev.polek.episodetracker.showdetails.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dev.polek.episodetracker.databinding.AboutShowFragmentBinding

class AboutShowFragment : Fragment() {

    private lateinit var binding: AboutShowFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View?
    {
        binding = AboutShowFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    companion object {
        fun instance() = AboutShowFragment()
    }
}
