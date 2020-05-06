package dev.polek.episodetracker.common.preferences.delegates

import com.russhwolf.settings.Settings
import dev.polek.episodetracker.common.logging.log
import kotlin.reflect.KProperty

class LongPreferenceDelegate(
    private val settings: Settings,
    private val key: String,
    private val defaultValue: Long)
{
    operator fun getValue(thisRef: Any?, property: KProperty<*>): Long {
        return settings.getLong(key, defaultValue)
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Long?) {
        log { "$key: $value" }

        if (value != null) {
            settings.putLong(key, value)
        } else {
            settings.remove(key)
        }
    }
}
