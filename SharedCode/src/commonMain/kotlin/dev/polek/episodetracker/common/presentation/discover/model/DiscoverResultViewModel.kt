package dev.polek.episodetracker.common.presentation.discover.model

import dev.polek.episodetracker.common.model.DiscoverResult

data class DiscoverResultViewModel(
    val id: Int,
    val name: String,
    val year: Int?,
    val posterUrl: String?,
    val overview: String,
    val genres: List<String>,
    var isInMyShows: Boolean,
    var isAddInProgress: Boolean = false)
{
    companion object {
        fun map(result: DiscoverResult, isInMyShows: Boolean): DiscoverResultViewModel {
            return DiscoverResultViewModel(
                id = result.tmdbId,
                name = result.name,
                year = result.year,
                posterUrl = result.posterUrl,
                overview = result.overview,
                genres = result.genres,
                isInMyShows = isInMyShows)
        }
    }
}
