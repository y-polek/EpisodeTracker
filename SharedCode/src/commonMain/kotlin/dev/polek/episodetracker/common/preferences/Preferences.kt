package dev.polek.episodetracker.common.preferences

import com.russhwolf.settings.Settings
import dev.polek.episodetracker.common.preferences.delegates.BooleanPreferenceDelegate

class Preferences(settings: Settings) {

    var isUpcomingExpanded: Boolean by BooleanPreferenceDelegate(
        settings = settings,
        key = KEY_IS_UPCOMING_EXPANDED,
        defaultValue = true)
    var isTbaExpanded: Boolean by BooleanPreferenceDelegate(
        settings = settings,
        key = KEY_IS_TBA_EXPANDED,
        defaultValue = true)
    var isEndedExpanded: Boolean by BooleanPreferenceDelegate(
        settings = settings,
        key = KEY_IS_ENDED_EXPANDED,
        defaultValue = true)
    var isArchivedExpanded: Boolean by BooleanPreferenceDelegate(
        settings = settings,
        key = KEY_IS_ARCHIVED_EXPANDED,
        defaultValue = false)

    companion object {
        const val KEY_IS_UPCOMING_EXPANDED = "key_is_upcoming_expanded"
        const val KEY_IS_TBA_EXPANDED = "key_is_tba_expanded"
        const val KEY_IS_ENDED_EXPANDED = "key_is_ended_expanded"
        const val KEY_IS_ARCHIVED_EXPANDED = "key_is_archived_expanded"
    }
}
