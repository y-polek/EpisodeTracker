package dev.polek.episodetracker.common.repositories

import dev.polek.episodetracker.common.datasource.themoviedb.TmdbService
import dev.polek.episodetracker.common.datasource.themoviedb.entities.VideoEntity
import dev.polek.episodetracker.common.model.Trailer

class ShowRepository(private val tmdbService: TmdbService) {

    suspend fun trailers(showTmdbId: Int): List<Trailer> {
        return tmdbService.videos(showTmdbId)
            .filter(VideoEntity::isValid)
            .filter(VideoEntity::isYoutubeVideo)
            .map { video ->
                Trailer(
                    name = video.name.orEmpty(),
                    youtubeKey = video.key.orEmpty(),
                    url = "https://www.youtube.com/watch?v=${video.key}",
                    previewImageUrl = "https://img.youtube.com/vi/${video.key}/mqdefault.jpg")
            }
    }
}
