package dev.polek.episodetracker.common.preferences

import com.russhwolf.settings.ExperimentalListener
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SettingsListener

@OptIn(ExperimentalListener::class)
expect fun Settings.listen(key: String, callback: () -> Unit): SettingsListener
