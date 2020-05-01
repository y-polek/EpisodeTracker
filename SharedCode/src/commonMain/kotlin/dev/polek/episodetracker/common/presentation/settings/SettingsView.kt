package dev.polek.episodetracker.common.presentation.settings

import dev.polek.episodetracker.common.model.Appearance

interface SettingsView {

    fun setAppearance(appearance: Appearance)
}
