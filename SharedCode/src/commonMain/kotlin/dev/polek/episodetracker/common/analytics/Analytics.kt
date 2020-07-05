package dev.polek.episodetracker.common.analytics

import dev.polek.episodetracker.common.model.Appearance

interface Analytics {

    fun logEvent(name: String, params: List<Param>)

    fun setUserProperty(name: String, value: String)

    fun removeUserProperty(name: String)

    fun logShare(text: String, screen: Screen) {
        logEvent("event_share") {
            param("text", text)
            param("screen", screen.name)
        }
    }

    fun logAddShow(tmdbId: Int, screen: Screen) {
        logEvent("event_add_shows") {
            param("tmdb_id", tmdbId)
            param("screen", screen.name)
        }
    }

    fun logRemoveShow(tmdbId: Int, screen: Screen) {
        logEvent("event_remove_shows") {
            param("tmdb_id", tmdbId)
            param("screen", screen.name)
        }
    }

    fun logArchiveShow(tmdbId: Int, screen: Screen) {
        logEvent("event_archive_show") {
            param("tmdb_id", tmdbId)
            param("screen", screen.name)
        }
    }

    fun logUnarchiveShow(tmdbId: Int, screen: Screen) {
        logEvent("event_unarchive_show") {
            param("tmdb_id", tmdbId)
            param("screen", screen.name)
        }
    }

    fun logOpenDetails(tmdbId: Int, screen: Screen) {
        logEvent("event_open_details") {
            param("tmdb_id", tmdbId)
            param("screen", screen.name)
        }
    }

    fun logOpenRecommendation(tmdbId: Int) {
        logEvent("event_open_recommendation") {
            param("tmdb_id", tmdbId)
        }
    }

    fun logAddRecommendation(tmdbId: Int) {
        logEvent("event_add_recommendation") {
            param("tmdb_id", tmdbId)
        }
    }

    fun logRemoveRecommendation(tmdbId: Int) {
        logEvent("event_remove_recommendation") {
            param("tmdb_id", tmdbId)
        }
    }

    fun logOpenTrailer(tmdbId: Int) {
        logEvent("open_trailer") {
            param("tmdb_id", tmdbId)
        }
    }

    fun logOpenCastMember(showTmdbId: Int) {
        logEvent("open_cast_member") {
            param("show_tmdb_id", showTmdbId)
        }
    }

    fun logOpenImdb(tmdbId: Int) {
        logEvent("event_open_imdb") {
            param("tmdb_id", tmdbId)
        }
    }

    fun logOpenHomePage(tmdbId: Int) {
        logEvent("event_open_home_page") {
            param("tmdb_id", tmdbId)
        }
    }

    fun logOpenInstagram(tmdbId: Int) {
        logEvent("event_open_instagram") {
            param("tmdb_id", tmdbId)
        }
    }

    fun logOpenFacebook(tmdbId: Int) {
        logEvent("event_open_facebook") {
            param("tmdb_id", tmdbId)
        }
    }

    fun logOpenTwitter(tmdbId: Int) {
        logEvent("event_open_twitter") {
            param("tmdb_id", tmdbId)
        }
    }

    fun logNumberOfShows(count: Int) {
        val category = when (count) {
            0 -> "0"
            in 1..2 -> "1-2"
            in 3..5 -> "3-5"
            in 6..10 -> "6-10"
            in 11..20 -> "11-20"
            in 21..30 -> "21-30"
            in 31..40 -> "31-40"
            in 41..50 -> "41-50"
            in 50..100 -> "50-100"
            else -> ">100"
        }
        setUserProperty("number_of_shows", category)
    }

    fun logAppearance(appearance: Appearance) {
        setUserProperty("current_appearance", appearance.name)
    }

    fun logShowLastWeekSection(showLastWeek: Boolean) {
        setUserProperty("last_week_section", showLastWeek)
    }

    fun logShowToWatchBadge(showBadge: Boolean) {
        setUserProperty("to_watch_badge", showBadge)
    }

    fun logShowSpecials(showSpecials: Boolean) {
        setUserProperty("show_specials", showSpecials)
    }

    fun logShowSpecialsInToWatchList(showSpecialsInToWatch: Boolean) {
        setUserProperty("specials_in_to_watch", showSpecialsInToWatch)
    }

    private inline fun logEvent(name: String, init: MutableList<Param>.() -> Unit) {
        val params = mutableListOf<Param>()
        params.init()
        logEvent(name, params)
    }

    private fun setUserProperty(name: String, value: Boolean) {
        setUserProperty(name, value.toString())
    }

    sealed class Param constructor(val key: String) {
        class StringParam(key: String, val value: String) : Param(key)
        class LongParam(key: String, val value: Long) : Param(key)
        class DoubleParam(key: String, val value: Double) : Param(key)
    }

    enum class Screen {
        MY_SHOWS,
        TO_WATCH,
        DISCOVER,
        SETTINGS,
        SHOW_DETAILS
    }

    private companion object {

        private fun MutableList<Param>.param(key: String, value: String) {
            add(Param.StringParam(key, value))
        }

        private fun MutableList<Param>.param(key: String, value: Int) {
            add(Param.LongParam(key, value.toLong()))
        }

        private fun MutableList<Param>.param(key: String, value: Long) {
            add(Param.LongParam(key, value))
        }

        private fun MutableList<Param>.param(key: String, value: Double) {
            add(Param.DoubleParam(key, value))
        }
    }
}