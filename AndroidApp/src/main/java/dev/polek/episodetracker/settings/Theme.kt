package dev.polek.episodetracker.settings

import androidx.annotation.StringRes
import dev.polek.episodetracker.R
import dev.polek.episodetracker.common.model.Appearance

enum class Theme(@StringRes val nameRes: Int) {

    LIGHT(R.string.prefs_light),
    DARK(R.string.prefs_dark),
    SYSTEM_DEFAULT(R.string.prefs_system_default);

    val appearance: Appearance
        get() = when (this) {
            LIGHT -> Appearance.LIGHT
            DARK -> Appearance.DARK
            SYSTEM_DEFAULT -> Appearance.AUTOMATIC
        }

    companion object {

        fun byAppearance(appearance: Appearance): Theme = when (appearance) {
            Appearance.LIGHT -> LIGHT
            Appearance.DARK -> DARK
            Appearance.AUTOMATIC -> SYSTEM_DEFAULT
        }
    }
}
