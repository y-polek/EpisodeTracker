package dev.polek.episodetracker.common.analytics

interface Analytics {

    fun logEvent(name: String, params: List<Param>)

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

    private inline fun logEvent(name: String, init: MutableList<Param>.() -> Unit) {
        val params = mutableListOf<Param>()
        params.init()
        logEvent(name, params)
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