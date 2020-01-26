package dev.polek.episodetracker.common.testutils

sealed class TmdbUrl {
    data class Show(val showId: Int) : TmdbUrl()
    data class Season(val showId: Int, val season: Int) : TmdbUrl()

    companion object {
        private val showRegex = "^.*/tv/([0-9]+)\\?append_to_response=external_ids$".toRegex()
        private val seasonRegex = "^.*/tv/([0-9]+)/season/([0-9]+)$".toRegex()

        fun parse(url: String): TmdbUrl? {
            return parseShow(url) ?: parseSeason(url)
        }

        private fun parseShow(url: String): Show? {
            val match = showRegex.matchEntire(url) ?: return null
            val showId = match.groupValues[1].toIntOrNull() ?: return null
            return Show(showId)
        }

        private fun parseSeason(url: String): Season? {
            val match = seasonRegex.matchEntire(url) ?: return null
            val showId = match.groupValues[1].toIntOrNull() ?: return null
            val season = match.groupValues[2].toIntOrNull() ?: return null
            return Season(showId = showId, season = season)
        }
    }
}
