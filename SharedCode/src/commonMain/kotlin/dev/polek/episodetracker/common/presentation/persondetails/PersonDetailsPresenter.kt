package dev.polek.episodetracker.common.presentation.persondetails

import dev.polek.episodetracker.common.datasource.themoviedb.TmdbService
import dev.polek.episodetracker.common.datasource.themoviedb.TmdbService.Companion.backdropImageUrl
import dev.polek.episodetracker.common.datasource.themoviedb.TmdbService.Companion.profileImageUrl
import dev.polek.episodetracker.common.presentation.BasePresenter
import dev.polek.episodetracker.common.presentation.showdetails.model.RecommendationViewModel
import kotlinx.coroutines.launch

class PersonDetailsPresenter(
    private val personId: Int,
    private val tmdbService: TmdbService) : BasePresenter<PersonDetailsView>()
{
    override fun attachView(view: PersonDetailsView) {
        super.attachView(view)
        loadPersonDetails()
    }

    fun onShowClicked(show: RecommendationViewModel) {
        view?.openShow(show)
    }

    private fun loadPersonDetails() {
        launch {
            try {
                val entity = tmdbService.personDetails(personId)
                if (!entity.isValid) throw RuntimeException("Person is not valid")

                val shows = entity.knownForShows.map { recommendation ->
                    RecommendationViewModel(
                        showId = recommendation.tmdbId ?: 0,
                        name = recommendation.name.orEmpty(),
                        imageUrl = recommendation.backdropPath?.let(::backdropImageUrl),
                        year = recommendation.year,
                        networks = recommendation.networks)
                }

                val person = PersonViewModel(
                    id = entity.tmdbId!!,
                    name = entity.name!!,
                    biography = entity.biography,
                    imageUrl = entity.profilePath?.let(::profileImageUrl),
                    birthDate = entity.birthday,
                    deathDate = entity.deathday,
                    birthPlace = entity.placeOfBirth,
                    shows = shows,
                    homePageUrl = entity.homepage,
                    imdbId = entity.externalIds?.imdbId,
                    instagramUsername = entity.externalIds?.instagramId,
                    facebookProfile = entity.externalIds?.facebookId,
                    twitterUsername = entity.externalIds?.twitterId)

                view?.displayPersonDetails(person)
            } catch (e: Throwable) {
                TODO("not implemented")
            }
        }
    }
}
