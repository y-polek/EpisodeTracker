package dev.polek.episodetracker.common.presentation.showdetails.model

class SeasonsViewModel(private val seasons: List<SeasonViewModel>) {

    private val seasonsMap: Map<Int, SeasonViewModel>

    init {
        seasonsMap = mutableMapOf()
        for (season in seasons) {
            seasonsMap[season.number] = season
        }
    }

    operator fun get(seasonNumber: Int): SeasonViewModel? {
        return seasonsMap[seasonNumber]
    }

    fun get(numberRange: IntRange): Sequence<SeasonViewModel> {
        return numberRange.asSequence().mapNotNull { seasonNumber -> get(seasonNumber) }
    }

    fun asList(): List<SeasonViewModel> = seasons
}
