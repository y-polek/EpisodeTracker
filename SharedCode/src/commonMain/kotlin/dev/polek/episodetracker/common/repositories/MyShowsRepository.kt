package dev.polek.episodetracker.common.repositories

import com.squareup.sqldelight.Query
import dev.polek.episodetracker.common.datasource.db.QueryListener
import dev.polek.episodetracker.common.datasource.db.QueryListener.Subscriber
import dev.polek.episodetracker.common.datasource.themoviedb.TmdbService
import dev.polek.episodetracker.common.datasource.themoviedb.TmdbService.Companion.backdropImageUrl
import dev.polek.episodetracker.common.datasource.themoviedb.TmdbService.Companion.networkImageUrl
import dev.polek.episodetracker.common.datasource.themoviedb.entities.GenreEntity
import dev.polek.episodetracker.common.logging.log
import dev.polek.episodetracker.common.presentation.myshows.model.MyShowsListItem.ShowViewModel
import dev.polek.episodetracker.common.presentation.myshows.model.MyShowsListItem.UpcomingShowViewModel
import dev.polek.episodetracker.common.utils.formatEpisodeNumber
import dev.polek.episodetracker.common.utils.formatTimeBetween
import dev.polek.episodetracker.db.Database
import dev.polek.episodetracker.db.ShowDetails
import io.ktor.util.date.GMTDate

class MyShowsRepository(
    private val db: Database,
    private val tmdbService: TmdbService,
    private val showRepository: ShowRepository)
{
    private var upcomingShowsQueryListener: QueryListener<UpcomingShowViewModel, List<UpcomingShowViewModel>>? = null
    private var toBeAnnouncedShowsQueryListener: QueryListener<ShowViewModel, List<ShowViewModel>>? = null
    private var endedShowsQueryListener: QueryListener<ShowViewModel, List<ShowViewModel>>? = null

    suspend fun addShow(tmdbId: Int) {
        if (isInMyShows(tmdbId)) return
        val show = tmdbService.show(tmdbId)
        check(show.isValid) { throw RuntimeException("Trying to add invalid show: $show") }

        log("Adding show: $show")

        val seasons = (1..show.numberOfSeasons).mapNotNull { seasonNumber ->
            showRepository.season(showTmdbId = tmdbId, seasonNumber = seasonNumber)
        }

        db.transaction {
            seasons.flatMap { it.episodes }
                .forEach { episode ->
                    db.episodeQueries.insert(
                        showTmdbId = tmdbId,
                        name = episode.name,
                        episodeNumber = episode.number.episode,
                        seasonNumber = episode.number.season,
                        airDateMillis = episode.airDateMillis,
                        imageUrl = episode.imageUrl)
                }

            db.myShowQueries.insert(
                tmdbId = tmdbId,
                imdbId = show.externalIds?.imdbId,
                tvdbId = show.externalIds?.tvdbId,
                facebookId = show.externalIds?.facebookId,
                instagramId = show.externalIds?.instagramId,
                twitterId = show.externalIds?.twitterId,
                name = show.name.orEmpty(),
                overview = show.overview.orEmpty(),
                year = show.year,
                lastYear = show.lastYear,
                imageUrl = show.backdropPath?.let(::backdropImageUrl),
                homePageUrl = show.homepage,
                genres = show.genres?.map(GenreEntity::name).orEmpty(),
                networks = show.networks,
                contentRating = show.contentRating,
                isEnded = show.isEnded,
                nextEpisodeSeason = show.nextEpisodeToAir?.seasonNumber,
                nextEpisodeNumber = show.nextEpisodeToAir?.episodeNumber)
        }
    }

    fun removeShow(tmdbId: Int) {
        db.transaction {
            db.episodeQueries.deleteByTmdbId(tmdbId)
            db.myShowQueries.deleteByTmdbId(tmdbId)
        }
    }

    fun isInMyShows(tmdbId: Int): Boolean {
        return db.myShowQueries.isInMyShows(tmdbId).executeAsOne()
    }

    fun upcomingShows(): List<UpcomingShowViewModel> {
        val now = GMTDate()
        return db.myShowQueries.upcomingShows { tmdbId, name, episodeName, episodeNumber, seasonNumber, airDateMillis, imageUrl ->
            val daysLeft: String = if (airDateMillis != null) {
                formatTimeBetween(now, GMTDate(airDateMillis))
            } else {
                "N/A"
            }

            val show = UpcomingShowViewModel(
                id = tmdbId,
                name = name,
                backdropUrl = imageUrl,
                episodeName = episodeName,
                episodeNumber = formatEpisodeNumber(season = seasonNumber, episode = episodeNumber),
                timeLeft = daysLeft)
            show
        }.executeAsList()
    }

    fun setUpcomingShowsSubscriber(subscriber: Subscriber<List<UpcomingShowViewModel>>) {
        removeUpcomingShowsSubscriber()

        val now = GMTDate()
        val query = db.myShowQueries.upcomingShows { tmdbId, name, episodeName, episodeNumber, seasonNumber, airDateMillis, imageUrl ->
            val daysLeft: String = if (airDateMillis != null) {
                formatTimeBetween(now, GMTDate(airDateMillis))
            } else {
                "N/A"
            }

            val show = UpcomingShowViewModel(
                id = tmdbId,
                name = name,
                backdropUrl = imageUrl,
                episodeName = episodeName,
                episodeNumber = formatEpisodeNumber(season = seasonNumber, episode = episodeNumber),
                timeLeft = daysLeft)
            show
        }

        upcomingShowsQueryListener = QueryListener(
            query = query,
            subscriber = subscriber,
            notifyImmediately = true,
            extractQueryResult = Query<UpcomingShowViewModel>::executeAsList)
    }

    fun removeUpcomingShowsSubscriber() {
        upcomingShowsQueryListener?.destroy()
        upcomingShowsQueryListener = null
    }

    fun toBeAnnouncedShows(): List<ShowViewModel> {
        return db.myShowQueries.toBeAnnouncedShows { tmdbId, name, imageUrl ->
            val show = ShowViewModel(
                id = tmdbId,
                name = name,
                backdropUrl = imageUrl)
            show
        }.executeAsList()
    }

    fun setToBeAnnouncedShowsSubscriber(subscriber: Subscriber<List<ShowViewModel>>) {
        removeToBeAnnouncedShowsSubscriber()

        toBeAnnouncedShowsQueryListener = QueryListener(
            query = db.myShowQueries.toBeAnnouncedShows(mapper = ::mapShowViewModel),
            subscriber = subscriber,
            notifyImmediately = true,
            extractQueryResult = Query<ShowViewModel>::executeAsList)
    }

    fun removeToBeAnnouncedShowsSubscriber() {
        toBeAnnouncedShowsQueryListener?.destroy()
        toBeAnnouncedShowsQueryListener = null
    }

    fun setEndedShowsSubscriber(subscriber: Subscriber<List<ShowViewModel>>) {
        removeEndedShowsSubscriber()

        endedShowsQueryListener = QueryListener(
            query = db.myShowQueries.endedShows(mapper = ::mapShowViewModel),
            subscriber = subscriber,
            notifyImmediately = true,
            extractQueryResult = Query<ShowViewModel>::executeAsList)
    }

    fun removeEndedShowsSubscriber() {
        endedShowsQueryListener?.destroy()
        endedShowsQueryListener = null
    }

    fun endedShows(): List<ShowViewModel> {
        return db.myShowQueries.endedShows { tmdbId, name, imageUrl ->
            val show = ShowViewModel(
                id = tmdbId,
                name = name,
                backdropUrl = imageUrl)
            show
        }.executeAsList()
    }

    fun showDetails(tmdbId: Int): ShowDetails? {
        return db.myShowQueries.showDetails(tmdbId).executeAsOneOrNull()
    }

    companion object {
        fun mapShowViewModel(tmdbId: Int, name: String, imageUrl: String?): ShowViewModel {
            return ShowViewModel(
                id = tmdbId,
                name = name,
                backdropUrl = imageUrl)
        }
    }
}
