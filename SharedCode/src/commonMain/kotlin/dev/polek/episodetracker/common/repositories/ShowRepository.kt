package dev.polek.episodetracker.common.repositories

import dev.polek.episodetracker.common.datasource.omdb.OmdbService
import dev.polek.episodetracker.common.datasource.themoviedb.TmdbService
import dev.polek.episodetracker.common.datasource.themoviedb.TmdbService.Companion.stillImageUrl
import dev.polek.episodetracker.common.datasource.themoviedb.entities.EpisodeEntity
import dev.polek.episodetracker.common.datasource.themoviedb.entities.SeasonEntity
import dev.polek.episodetracker.common.datasource.themoviedb.entities.ShowDetailsEntity
import dev.polek.episodetracker.common.model.Episode
import dev.polek.episodetracker.common.model.EpisodeNumber
import dev.polek.episodetracker.common.model.Season

class ShowRepository(
    private val tmdbService: TmdbService,
    private val omdbService: OmdbService) {

    suspend fun showDetails(showTmdbId: Int): ShowDetailsEntity {
        return tmdbService.showDetails(showTmdbId)
    }

    suspend fun imdbRating(imdbId: String): Float? {
        return omdbService.show(imdbId).imdbRating
    }

    suspend fun season(showTmdbId: Int, seasonNumber: Int): Season? {
        val season = tmdbService.season(showTmdbId = showTmdbId, number = seasonNumber)
        if (!season.isValid) return null

        return mapSeason(season)
    }

    companion object {

        private fun mapEpisode(episode: EpisodeEntity): Episode {
            return Episode(
                name = episode.name.orEmpty(),
                number = EpisodeNumber(
                    season = episode.seasonNumber ?: 0,
                    episode = episode.episodeNumber ?: 0),
                airDateMillis = episode.airDateMillis,
                imageUrl = episode.stillPath?.let(::stillImageUrl),
                isWatched = false
            )
        }

        private fun mapSeason(season: SeasonEntity): Season {
            return Season(
                number = season.number ?: 0,
                episodes = season.episodes?.filter(EpisodeEntity::isValid)?.map(::mapEpisode).orEmpty()
            )
        }
    }
}
