package dev.polek.episodetracker.common.presentation.showdetails.model

data class SeasonViewModel(
    val number: Int,
    val episodes: List<EpisodeViewModel>)
{
    var isExpanded: Boolean = false

    var isWatched: Boolean
        get() = episodes.all(EpisodeViewModel::isWatched)
        set(value) = episodes.forEach { episode ->
            if (episode.isAired) {
                episode.isWatched = value
            }
        }

    val watchedEpisodes: String
        get() {
            val totalCount = episodes.size
            val watchedCount = episodes.count(EpisodeViewModel::isWatched)
            return "$watchedCount/$totalCount"
        }
}
