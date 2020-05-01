package dev.polek.episodetracker.common.preferences

import com.russhwolf.settings.Settings
import dev.polek.episodetracker.common.model.Appearance
import dev.polek.episodetracker.common.preferences.delegates.BooleanPreferenceDelegate
import dev.polek.episodetracker.common.preferences.delegates.EnumPreferenceDelegate

class Preferences(settings: Settings) {
    var appearance: Appearance by EnumPreferenceDelegate(
        settings = settings,
        key = KEY_APPEARANCE,
        defaultValue = Appearance.AUTOMATIC,
        enumValues = Appearance.values())

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
    var showLastWeekSection: Boolean by BooleanPreferenceDelegate(
        settings = settings,
        key = KEY_SHOW_LAST_WEEK_SECTION,
        defaultValue = true)

    companion object {
        const val KEY_APPEARANCE = "key_appearance"

        const val KEY_IS_UPCOMING_EXPANDED = "key_is_upcoming_expanded"
        const val KEY_IS_TBA_EXPANDED = "key_is_tba_expanded"
        const val KEY_IS_ENDED_EXPANDED = "key_is_ended_expanded"
        const val KEY_IS_ARCHIVED_EXPANDED = "key_is_archived_expanded"
        const val KEY_SHOW_LAST_WEEK_SECTION = "key_show_last_week_section"
    }
}
