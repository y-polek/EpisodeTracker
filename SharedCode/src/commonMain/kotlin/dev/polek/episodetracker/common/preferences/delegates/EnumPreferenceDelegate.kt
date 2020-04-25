package dev.polek.episodetracker.common.preferences.delegates

import com.russhwolf.settings.Settings
import kotlin.reflect.KProperty

internal class EnumPreferenceDelegate<T: Enum<T>>(
    private val settings: Settings,
    private val key: String,
    private val defaultValue: T,
    private val enumValues: Array<T>)
{
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        val name = settings.getStringOrNull(key) ?: return defaultValue
        return enumValues.find { it.name == name } ?: defaultValue
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        if (value != null) {
            settings.putString(key, value.name)
        } else {
            settings.remove(key)
        }
    }
}