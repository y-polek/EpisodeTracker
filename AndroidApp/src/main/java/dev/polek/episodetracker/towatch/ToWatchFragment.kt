package dev.polek.episodetracker.towatch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dev.polek.episodetracker.App
import dev.polek.episodetracker.common.model.EpisodeNumber
import dev.polek.episodetracker.common.presentation.towatch.ToWatchShowViewModel
import dev.polek.episodetracker.common.presentation.towatch.ToWatchView
import dev.polek.episodetracker.databinding.ToWatchFragmentBinding
import dev.polek.episodetracker.utils.HideKeyboardScrollListener

class ToWatchFragment : Fragment(), ToWatchView {

    private val presenter = App.instance.di.toWatchPresenter()

    private lateinit var binding: ToWatchFragmentBinding
    private val adapter = ToWatchAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View?
    {
        val binding = ToWatchFragmentBinding.inflate(inflater)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            addOnScrollListener(HideKeyboardScrollListener())
            setHasFixedSize(true)
        }
        binding.recyclerView.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this)
    }

    override fun onDestroyView() {
        presenter.detachView()
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        presenter.onViewAppeared()
    }

    override fun onPause() {
        presenter.onViewDisappeared()
        super.onPause()
    }

    //region ToWatchView implementation

    override fun displayShows(shows: List<ToWatchShowViewModel>) {
        adapter.shows = shows
    }

    override fun showEmptyMessage(isFiltered: Boolean) {
        // TODO("not implemented")
    }

    override fun hideEmptyMessage() {
        // TODO("not implemented")
    }

    override fun openToWatchShowDetails(show: ToWatchShowViewModel, episode: EpisodeNumber) {
        // TODO("not implemented")
    }
    //endregion

    companion object {
        fun instance() = ToWatchFragment()
    }
}
