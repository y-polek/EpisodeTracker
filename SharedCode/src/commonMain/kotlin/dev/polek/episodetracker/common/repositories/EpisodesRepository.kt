package dev.polek.episodetracker.common.repositories

import dev.polek.episodetracker.common.presentation.showdetails.model.EpisodeViewModel
import dev.polek.episodetracker.common.presentation.showdetails.model.SeasonViewModel
import dev.polek.episodetracker.common.utils.formatDate
import dev.polek.episodetracker.db.Database
import io.ktor.util.date.GMTDate

class EpisodesRepository(private val db: Database) {

    fun allSeasons(showTmdbId: Int): List<SeasonViewModel> {
        val allEpisodes = db.episodeQueries.episodes(showTmdbId) { name, seasonNumber, episodeNumber, isWatched, airDateMillis, imageUrl ->
            EpisodeViewModel(
                number = episodeNumber,
                season = seasonNumber,
                name = name,
                airDate = airDateMillis?.let { formatDate(GMTDate(it)) }.orEmpty(),
                imageUrl = imageUrl,
                isWatched = isWatched)
        }.executeAsList()

        return allEpisodes.groupBy(EpisodeViewModel::season)
            .map { (season, episodes) ->
                SeasonViewModel(
                    number = season,
                    episodes = episodes.sortedBy(EpisodeViewModel::number))
            }
            .sortedBy(SeasonViewModel::number)
    }
}
