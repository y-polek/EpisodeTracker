package dev.polek.episodetracker.common.preferences.delegates

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import dev.polek.episodetracker.common.logging.log
import kotlin.reflect.KProperty

class StringPreferenceDelegate(
    private val settings: Settings,
    private val key: String)
{
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String? {
        return settings.getStringOrNull(key)
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String?) {
        log { "$key: $value" }

        settings[key] = value
    }
}
