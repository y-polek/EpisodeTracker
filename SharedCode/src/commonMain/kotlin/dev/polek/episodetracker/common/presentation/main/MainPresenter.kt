package dev.polek.episodetracker.common.presentation.main

import com.russhwolf.settings.ExperimentalListener
import com.russhwolf.settings.SettingsListener
import dev.polek.episodetracker.common.datasource.db.QueryListener.Subscriber
import dev.polek.episodetracker.common.preferences.Preferences
import dev.polek.episodetracker.common.presentation.BasePresenter
import dev.polek.episodetracker.common.repositories.ToWatchRepository

class MainPresenter(
    private val toWatchRepository: ToWatchRepository,
    private val preferences: Preferences) : BasePresenter<MainView>() {

    private var numberOfToWatchEpisodesSubscriber = object : Subscriber<Long> {
        override fun onQueryResult(result: Long) {
            when {
                result > 999 -> view?.showToWatchBadge("999+")
                result > 0 -> view?.showToWatchBadge(result.toString())
                else -> view?.hideToWatchBadge()
            }
        }
    }

    @OptIn(ExperimentalListener::class)
    private var showSpecialsListener: SettingsListener? = null

    override fun onViewAppeared() {
        super.onViewAppeared()

        toWatchRepository.setNumberOfToWatchEpisodesSubscriber(numberOfToWatchEpisodesSubscriber)

        showSpecialsListener = preferences.listenShowSpecialsInToWatch {
            toWatchRepository.setNumberOfToWatchEpisodesSubscriber(numberOfToWatchEpisodesSubscriber)
        }
    }

    @OptIn(ExperimentalListener::class)
    override fun onViewDisappeared() {
        showSpecialsListener?.deactivate()
        toWatchRepository.removeNumberOfToWatchEpisodesSubscriber()

        super.onViewDisappeared()
    }
}
