package dev.polek.episodetracker.common.repositories

import dev.polek.episodetracker.common.model.Episode
import dev.polek.episodetracker.common.model.EpisodeNumber
import dev.polek.episodetracker.common.model.Season
import dev.polek.episodetracker.db.Database

class EpisodesRepository(private val db: Database) {

    fun allSeasons(showTmdbId: Int): List<Season> {
        val allEpisodes = db.episodeQueries.episodes(showTmdbId) { name, seasonNumber, episodeNumber, isWatched, airDateMillis, imageUrl ->
            Episode(
                name = name,
                number = EpisodeNumber(season = seasonNumber, episode = episodeNumber),
                airDateMillis = airDateMillis,
                imageUrl = imageUrl,
                isWatched = isWatched)
        }.executeAsList()

        return allEpisodes.groupBy { it.number.season }
            .map { (season, episodes) ->
                Season(
                    number = season,
                    episodes = episodes.sortedBy { it.number.episode })
            }
            .sortedBy(Season::number)
    }

    fun setEpisodeWatched(showTmdbId: Int, seasonNumber: Int, episodeNumber: Int, isWatched: Boolean) {
        db.episodeQueries.setEpisodeWatched(
            showTmdbId = showTmdbId,
            seasonNumber = seasonNumber,
            episodeNumber = episodeNumber,
            isWatched = isWatched)
    }

    fun markNextEpisodeWatched(showTmdbId: Int) {
        db.transaction {
            val nextEpisode = db.episodeQueries.nextNotWatchedEpisode(showTmdbId)
                .executeAsOneOrNull()
                ?: return@transaction

            db.episodeQueries.setEpisodeWatched(
                showTmdbId = showTmdbId,
                seasonNumber = nextEpisode.seasonNumber,
                episodeNumber = nextEpisode.episodeNumber,
                isWatched = true)
        }
    }

    fun markNextSpecialEpisodeWatched(showTmdbId: Int) {
        db.transaction {
            val nextEpisode = db.episodeQueries.nextNotWatchedSpecialEpisode(showTmdbId)
                .executeAsOneOrNull()
                ?: return@transaction

            db.episodeQueries.setEpisodeWatched(
                showTmdbId = showTmdbId,
                seasonNumber = nextEpisode.seasonNumber,
                episodeNumber = nextEpisode.episodeNumber,
                isWatched = true)
        }
    }

    fun markAllWatched(showTmdbId: Int) {
        db.episodeQueries.markAllWatched(showTmdbId)
    }

    fun markAllSpecialsWatched(showTmdbId: Int) {
        db.episodeQueries.markAllSpecialsWatched(showTmdbId)
    }

    fun markAllWatchedUpTo(showTmdbId: Int, episodeNumber: EpisodeNumber) {
        db.episodeQueries.markAllWatchedUpTo(
            showTmdbId = showTmdbId,
            season = episodeNumber.season,
            episode = episodeNumber.episode)
    }

    fun markAllWatchedUpToSeason(showTmdbId: Int, season: Int) {
        db.episodeQueries.markAllWatchedUpToSeason(
            showTmdbId = showTmdbId,
            season = season)
    }

    fun setSeasonWatched(showTmdbId: Int, seasonNumber: Int, isWatched: Boolean) {
        db.episodeQueries.setSeasonWatched(
            showTmdbId = showTmdbId,
            seasonNumber = seasonNumber,
            isWatched = isWatched)
    }

    fun firstNotWatchedEpisode(tmdbShowId: Int): EpisodeNumber? {
        return db.episodeQueries.nextNotWatchedEpisode(tmdbShowId) { seasonNumber, episodeNumber ->
            EpisodeNumber(season = seasonNumber, episode = episodeNumber)
        }.executeAsOneOrNull()
    }
}
