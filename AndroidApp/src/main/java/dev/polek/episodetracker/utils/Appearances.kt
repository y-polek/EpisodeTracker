package dev.polek.episodetracker.utils

import androidx.appcompat.app.AppCompatDelegate
import dev.polek.episodetracker.common.model.Appearance
import dev.polek.episodetracker.common.model.Appearance.*

fun Appearance.apply() {
    val mode = when (this) {
        LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
        DARK -> AppCompatDelegate.MODE_NIGHT_YES
        AUTOMATIC -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    }
    AppCompatDelegate.setDefaultNightMode(mode)
}
