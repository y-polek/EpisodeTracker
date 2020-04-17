package dev.polek.episodetracker.common.preferences

import com.russhwolf.settings.AppleSettings
import com.russhwolf.settings.Settings
import platform.Foundation.NSUserDefaults

val settings: Settings = AppleSettings(NSUserDefaults.standardUserDefaults)
