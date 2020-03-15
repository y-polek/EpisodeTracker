package dev.polek.episodetracker.common.repositories

import com.squareup.sqldelight.Query
import dev.polek.episodetracker.common.coroutines.ui
import dev.polek.episodetracker.common.datasource.db.QueryListener
import dev.polek.episodetracker.common.datasource.themoviedb.TmdbService
import dev.polek.episodetracker.common.datasource.themoviedb.entities.GenreEntity
import dev.polek.episodetracker.common.datasource.themoviedb.entities.ShowEntity
import dev.polek.episodetracker.common.logging.log
import dev.polek.episodetracker.common.logging.logw
import dev.polek.episodetracker.common.model.Season
import dev.polek.episodetracker.db.AddToMyShowsTask
import dev.polek.episodetracker.db.Database
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class AddToMyShowsQueue(
    private val db: Database,
    private val tmdbService: TmdbService,
    private val showRepository: ShowRepository) : CoroutineScope {

    private val queryListener: QueryListener<AddToMyShowsTask, List<AddToMyShowsTask>>
    private var executeTaskJob: Job? = null

    init {
        db.addToMyShowsTaskQueries.clearProgress()

        queryListener = QueryListener(
            query = db.addToMyShowsTaskQueries.allTasks(),
            subscriber = object : QueryListener.Subscriber<List<AddToMyShowsTask>> {
                override fun onQueryResult(result: List<AddToMyShowsTask>) {
                    val inProgressTasks = result.filter { it.inProgress }.joinToString { it.showTmdbId.toString() }
                    val waitingTasks = result.filter { !it.inProgress }.joinToString { it.showTmdbId.toString() }
                    log("Tasks. In Progress: $inProgressTasks, Waiting: $waitingTasks")

                    onTaskListModified(result)
                }
            },
            notifyImmediately = true,
            extractQueryResult = Query<AddToMyShowsTask>::executeAsList)
    }

    override val coroutineContext: CoroutineContext
        get() {
            return ui + Job()
        }

    fun addShow(tmdbId: Int) {
        val alreadyInQueue = db.addToMyShowsTaskQueries.taskExist(tmdbId).executeAsOne()
        if (alreadyInQueue) {
            logw("Trying to add Show that's already in Queue")
            return
        }

        db.addToMyShowsTaskQueries.add(showTmdbId = tmdbId)
    }

    fun cancelAddIfExist(tmdbId: Int) {
        val task = db.addToMyShowsTaskQueries.task(tmdbId).executeAsOneOrNull() ?: return

        if (task.inProgress) {
            executeTaskJob?.cancel()
        }

        db.addToMyShowsTaskQueries.remove(tmdbId)
    }

    private fun onTaskListModified(tasks: List<AddToMyShowsTask>) {
        if (tasks.isEmpty()) return

        val hasTaskInProgress = tasks.find(AddToMyShowsTask::inProgress) != null
        if (hasTaskInProgress) return

        executeTask(tasks.first())
    }

    private fun executeTask(task: AddToMyShowsTask) {
        log("Tasks. executeTask: ${task.showTmdbId}")

        val showTmdbId = task.showTmdbId

        db.addToMyShowsTaskQueries.setInProgress(showTmdbId = showTmdbId, inProgress = true)

        executeTaskJob = launch {
            try {
                val show = tmdbService.show(showTmdbId)
                check(show.isValid) { throw RuntimeException("Can't add invalid show: $show") }

                if (!isActive) return@launch

                val seasons = (1..show.numberOfSeasons).mapNotNull { seasonNumber ->
                    showRepository.season(showTmdbId = showTmdbId, seasonNumber = seasonNumber)
                }

                if (!isActive) return@launch

                writeShowToDb(show, seasons)
                db.addToMyShowsTaskQueries.remove(showTmdbId)
            } catch (e: Throwable) {
                log("Tasks. Error: $e")
                if (isActive) {
                    db.addToMyShowsTaskQueries.setInProgress(showTmdbId = showTmdbId, inProgress = false)
                }
            }
        }
    }

    private fun writeShowToDb(show: ShowEntity, seasons: List<Season>) {
        val showTmdbId = show.tmdbId!!

        db.transaction {
            seasons.flatMap { it.episodes }
                .forEach { episode ->
                    db.episodeQueries.insert(
                        showTmdbId = showTmdbId,
                        name = episode.name,
                        episodeNumber = episode.number.episode,
                        seasonNumber = episode.number.season,
                        airDateMillis = episode.airDateMillis,
                        imageUrl = episode.imageUrl)
                }

            db.myShowQueries.insert(
                tmdbId = showTmdbId,
                imdbId = show.externalIds?.imdbId,
                tvdbId = show.externalIds?.tvdbId,
                facebookId = show.externalIds?.facebookId,
                instagramId = show.externalIds?.instagramId,
                twitterId = show.externalIds?.twitterId,
                name = show.name.orEmpty(),
                overview = show.overview.orEmpty(),
                year = show.year,
                lastYear = show.lastYear,
                imageUrl = show.backdropPath?.let(TmdbService.Companion::backdropImageUrl),
                homePageUrl = show.homepage,
                genres = show.genres?.map(GenreEntity::name).orEmpty(),
                networks = show.networks,
                contentRating = show.contentRating,
                isEnded = show.isEnded,
                nextEpisodeSeason = show.nextEpisodeToAir?.seasonNumber,
                nextEpisodeNumber = show.nextEpisodeToAir?.episodeNumber)
        }
    }
}
