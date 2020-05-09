package dev.polek.episodetracker.common.presentation.showdetails.model

import dev.polek.episodetracker.common.model.Season

data class SeasonViewModel(
    val number: Int,
    val name: String,
    val episodes: List<EpisodeViewModel>)
{
    var isExpanded: Boolean = false

    var isWatched: Boolean
        get() = episodes.all(EpisodeViewModel::isWatched)
        set(value) = episodes.forEach { episode ->
            if (episode.isAired || episode.isSpecial) {
                episode.isWatched = value
            }
        }

    val watchedEpisodes: String
        get() {
            val totalCount = episodes.size
            val watchedCount = episodes.count(EpisodeViewModel::isWatched)
            return "$watchedCount/$totalCount"
        }

    companion object {
        fun map(season: Season): SeasonViewModel {
            val name = if (season.number == 0) "Specials" else "Season ${season.number}"
            return SeasonViewModel(
                number = season.number,
                name = name,
                episodes = EpisodeViewModel.map(season.episodes))
        }
    }
}
