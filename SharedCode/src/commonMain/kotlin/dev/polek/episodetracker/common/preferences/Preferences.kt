package dev.polek.episodetracker.common.preferences

import com.russhwolf.settings.ExperimentalListener
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SettingsListener
import dev.polek.episodetracker.common.di.Inject
import dev.polek.episodetracker.common.model.Appearance
import dev.polek.episodetracker.common.preferences.delegates.BooleanPreferenceDelegate
import dev.polek.episodetracker.common.preferences.delegates.EnumPreferenceDelegate
import dev.polek.episodetracker.common.preferences.delegates.LongPreferenceDelegate

class Preferences @Inject constructor(private val settings: Settings) {

    var appearance: Appearance by EnumPreferenceDelegate(settings, KEY_APPEARANCE,
        defaultValue = Appearance.AUTOMATIC,
        enumValues = Appearance.values())

    var lastRefreshTimestamp: Long by LongPreferenceDelegate(settings, KEY_LAST_REFRESH_TIMESTAMP, 0L)

    var isLastWeekExpanded by BooleanPreferenceDelegate(settings, KEY_IS_LAST_WEEK_EXPANDED, defaultValue = true)
    var isUpcomingExpanded by BooleanPreferenceDelegate(settings, KEY_IS_UPCOMING_EXPANDED, defaultValue = true)
    var isTbaExpanded by BooleanPreferenceDelegate(settings, KEY_IS_TBA_EXPANDED, defaultValue = true)
    var isEndedExpanded by BooleanPreferenceDelegate(settings, KEY_IS_ENDED_EXPANDED, defaultValue = true)
    var isArchivedExpanded by BooleanPreferenceDelegate(settings, KEY_IS_ARCHIVED_EXPANDED, defaultValue = false)

    var showLastWeekSection by BooleanPreferenceDelegate(settings, KEY_SHOW_LAST_WEEK_SECTION, defaultValue = true)

    var showToWatchBadge by BooleanPreferenceDelegate(settings, KEY_SHOW_TO_WATCH_BADGE, defaultValue = true)

    var showSpecials by BooleanPreferenceDelegate(settings, KEY_SHOW_SPECIALS, defaultValue = false)
    var showSpecialsInToWatch by BooleanPreferenceDelegate(settings, KEY_SHOW_SPECIALS_IN_TO_WATCH, defaultValue = false)

    @OptIn(ExperimentalListener::class)
    fun listenShowToWatchBadge(callback: () -> Unit): SettingsListener {
        return settings.listen(KEY_SHOW_TO_WATCH_BADGE, callback)
    }

    @OptIn(ExperimentalListener::class)
    fun listenShowSpecialsInToWatch(callback: () -> Unit): SettingsListener {
        return settings.listen(KEY_SHOW_SPECIALS_IN_TO_WATCH, callback)
    }

    companion object {
        private const val KEY_APPEARANCE = "key_appearance"

        private const val KEY_LAST_REFRESH_TIMESTAMP = "key_last_refresh_timestamp"

        private const val KEY_IS_LAST_WEEK_EXPANDED = "key_is_last_week_expanded"
        private const val KEY_IS_UPCOMING_EXPANDED = "key_is_upcoming_expanded"
        private const val KEY_IS_TBA_EXPANDED = "key_is_tba_expanded"
        private const val KEY_IS_ENDED_EXPANDED = "key_is_ended_expanded"
        private const val KEY_IS_ARCHIVED_EXPANDED = "key_is_archived_expanded"

        private const val KEY_SHOW_LAST_WEEK_SECTION = "key_show_last_week_section"

        private const val KEY_SHOW_TO_WATCH_BADGE = "key_show_to_watch_badge"

        private const val KEY_SHOW_SPECIALS = "key_show_specials"
        private const val KEY_SHOW_SPECIALS_IN_TO_WATCH = "key_show_specials_in_to_watch"
    }
}
