@file:JvmName("SettingsActual")
package dev.polek.episodetracker.common.preferences

import com.russhwolf.settings.AndroidSettings
import com.russhwolf.settings.ExperimentalListener
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SettingsListener

@OptIn(ExperimentalListener::class)
actual fun Settings.listen(key: String, callback: () -> Unit): SettingsListener {
    return (this as AndroidSettings).addListener(key, callback)
}

