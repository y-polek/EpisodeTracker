package dev.polek.episodetracker.common.presentation.persondetails

import dev.polek.episodetracker.common.datasource.themoviedb.TmdbService
import dev.polek.episodetracker.common.presentation.BasePresenter
import kotlinx.coroutines.launch

class PersonDetailsPresenter(
    private val personId: Int,
    private val tmdbService: TmdbService) : BasePresenter<PersonDetailsView>()
{

    override fun attachView(view: PersonDetailsView) {
        super.attachView(view)
        loadPersonDetails()
    }

    private fun loadPersonDetails() {
        launch {
            try {
                val entity = tmdbService.personDetails(personId)
                if (!entity.isValid) throw RuntimeException("Person is not valid")

                val person = PersonViewModel(
                    id = entity.tmdbId!!,
                    name = entity.name!!,
                    biography = entity.biography,
                    imageUrl = entity.profilePath?.let(TmdbService.Companion::profileImageUrl),
                    birthDate = entity.birthday,
                    deathDate = entity.deathday,
                    birthPlace = entity.placeOfBirth,
                    shows = emptyList(),
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
