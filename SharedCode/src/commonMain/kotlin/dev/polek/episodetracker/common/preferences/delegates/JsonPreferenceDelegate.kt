package dev.polek.episodetracker.common.preferences.delegates

import com.russhwolf.settings.Settings
import dev.polek.episodetracker.common.logging.log
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.serializer
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class JsonPreferenceDelegate<T: Any>(
    private val settings: Settings,
    private val key: String,
    private val type: KClass<T>)
{
    private val json = Json(JsonConfiguration.Stable)

    @OptIn(ImplicitReflectionSerializer::class)
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        val jsonValue = settings.getStringOrNull(key) ?: return null
        return json.parse(type.serializer(), jsonValue)
    }

    @OptIn(ImplicitReflectionSerializer::class)
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        log { "$key: $value" }

        if (value == null) {
            settings.remove(key)
        } else {
            val jsonValue = json.stringify(type.serializer(), value)
            settings.putString(key, jsonValue)
        }
    }
}
