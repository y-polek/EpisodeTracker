package dev.polek.episodetracker.common.presentation.settings

import dev.polek.episodetracker.common.model.Appearance
import dev.polek.episodetracker.common.preferences.Preferences
import dev.polek.episodetracker.common.presentation.BasePresenter

class SettingsPresenter(private val prefs: Preferences) : BasePresenter<SettingsView>() {

    override fun attachView(view: SettingsView) {
        super.attachView(view)
        view.setAppearance(prefs.appearance)
    }

    fun onAppearanceOptionClicked(appearance: Appearance) {
        if (appearance == prefs.appearance) return

        prefs.appearance = appearance
        view?.setAppearance(appearance)
    }
}
