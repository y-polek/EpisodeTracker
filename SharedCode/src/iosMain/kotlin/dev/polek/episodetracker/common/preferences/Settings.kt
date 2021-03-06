package dev.polek.episodetracker.common.preferences

import com.russhwolf.settings.AppleSettings
import com.russhwolf.settings.ExperimentalListener
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SettingsListener
import platform.Foundation.NSUserDefaults

val settings: Settings = AppleSettings(NSUserDefaults.standardUserDefaults)

@OptIn(ExperimentalListener::class)
actual fun Settings.listen(key: String, callback: () -> Unit): SettingsListener {
    return (this as AppleSettings).addListener(key, callback)
}
