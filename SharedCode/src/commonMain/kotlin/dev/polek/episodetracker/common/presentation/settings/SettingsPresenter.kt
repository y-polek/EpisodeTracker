package dev.polek.episodetracker.common.presentation.settings

import dev.polek.episodetracker.common.model.Appearance
import dev.polek.episodetracker.common.preferences.Preferences
import dev.polek.episodetracker.common.presentation.BasePresenter

class SettingsPresenter(private val prefs: Preferences) : BasePresenter<SettingsView>() {

    override fun attachView(view: SettingsView) {
        super.attachView(view)

        view.setAppearance(prefs.appearance)

        view.setShowLastWeekSection(prefs.showLastWeekSection)

        val showSpecials = prefs.showSpecials
        view.setShowSpecials(showSpecials)
        view.setShowSpecialsInToWatch(prefs.showSpecialsInToWatch, isEnabled = showSpecials)
    }

    fun onAppearanceOptionClicked(appearance: Appearance) {
        if (appearance == prefs.appearance) return

        prefs.appearance = appearance
        view?.setAppearance(appearance)
    }

    fun onShowLastWeekSectionChanged(isChecked: Boolean) {
        prefs.showLastWeekSection = isChecked
    }

    fun onShowSpecialsChanged(isChecked: Boolean) {
        prefs.showSpecials = isChecked
        if (!isChecked) {
            prefs.showSpecialsInToWatch = false
        }
        view?.setShowSpecialsInToWatch(prefs.showSpecialsInToWatch, isEnabled = isChecked)
    }

    fun onShowSpecialsInToWatchChanged(isChecked: Boolean) {
        prefs.showSpecialsInToWatch = isChecked
    }
}
