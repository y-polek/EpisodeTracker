package dev.polek.episodetracker.common.presentation.settings

import dev.polek.episodetracker.common.model.Appearance

interface SettingsView {

    fun setAppearance(appearance: Appearance)
    fun setShowLastWeekSection(showLastWeekSection: Boolean)
    fun setShowToWatchBadge(showBadge: Boolean)
    fun setShowSpecials(showSpecials: Boolean)
    fun setShowSpecialsInToWatch(showSpecialsInToWatch: Boolean, isEnabled: Boolean)
}
