package dev.polek.episodetracker.common.presentation.main

import com.russhwolf.settings.ExperimentalListener
import com.russhwolf.settings.SettingsListener
import dev.polek.episodetracker.common.datasource.db.QueryListener.Subscriber
import dev.polek.episodetracker.common.di.Inject
import dev.polek.episodetracker.common.preferences.Preferences
import dev.polek.episodetracker.common.presentation.BasePresenter
import dev.polek.episodetracker.common.repositories.ToWatchRepository

class MainPresenter @Inject constructor(
    private val toWatchRepository: ToWatchRepository,
    private val preferences: Preferences) : BasePresenter<MainView>() {

    private var numberOfToWatchEpisodesSubscriber = object : Subscriber<Long> {
        override fun onQueryResult(result: Long) {
            when {
                result > 0 -> view?.showToWatchBadge(result.toInt())
                else -> view?.hideToWatchBadge()
            }
        }
    }

    @OptIn(ExperimentalListener::class)
    private var showSpecialsListener: SettingsListener? = null

    @OptIn(ExperimentalListener::class)
    private var showToWatchBadge: SettingsListener? = null

    override fun onViewAppeared() {
        super.onViewAppeared()

        showToWatchBadge = preferences.listenShowToWatchBadge {
            setupBadge()
        }

        setupBadge()
    }

    @OptIn(ExperimentalListener::class)
    override fun onViewDisappeared() {
        showToWatchBadge?.deactivate()
        showSpecialsListener?.deactivate()
        toWatchRepository.removeNumberOfToWatchEpisodesSubscriber()

        super.onViewDisappeared()
    }

    private fun setupBadge() {
        if (preferences.showToWatchBadge) {
            showBadge()
        } else {
            hideBadge()
        }
    }

    private fun showBadge() {
        toWatchRepository.setNumberOfToWatchEpisodesSubscriber(numberOfToWatchEpisodesSubscriber)
        showSpecialsListener = preferences.listenShowSpecialsInToWatch {
            toWatchRepository.setNumberOfToWatchEpisodesSubscriber(numberOfToWatchEpisodesSubscriber)
        }
    }

    @OptIn(ExperimentalListener::class)
    private fun hideBadge() {
        toWatchRepository.removeNumberOfToWatchEpisodesSubscriber()
        showSpecialsListener?.deactivate()
        view?.hideToWatchBadge()
    }
}
