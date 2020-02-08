package dev.polek.episodetracker.common.presentation.showdetails.model

/**
 * @param episodes must be sorted by episode number
 */
class EpisodesViewModel(private val episodes: List<EpisodeViewModel>)
{
    private val episodesMap: Map<Int, EpisodeViewModel>

    init {
        episodesMap = mutableMapOf()
        for (episode in episodes) {
            episodesMap[episode.number.episode] = episode
        }
    }

    operator fun get(episodeNumber: Int): EpisodeViewModel? {
        return episodesMap[episodeNumber]
    }

    fun get(numberRange: IntRange): Sequence<EpisodeViewModel> {
        return numberRange.asSequence().mapNotNull { episodeNumber -> get(episodeNumber) }
    }

    fun asList(): List<EpisodeViewModel> = episodes
}

fun List<EpisodeViewModel>.toEpisodes(): EpisodesViewModel {
    return EpisodesViewModel(this.sortedBy { it.number.episode })
}
