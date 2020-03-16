package dev.polek.episodetracker.common.repositories

import com.squareup.sqldelight.Query
import dev.polek.episodetracker.common.coroutines.ui
import dev.polek.episodetracker.common.datasource.db.QueryListener
import dev.polek.episodetracker.common.datasource.themoviedb.TmdbService
import dev.polek.episodetracker.common.datasource.themoviedb.entities.GenreEntity
import dev.polek.episodetracker.common.datasource.themoviedb.entities.ShowEntity
import dev.polek.episodetracker.common.logging.log
import dev.polek.episodetracker.common.logging.loge
import dev.polek.episodetracker.common.logging.logw
import dev.polek.episodetracker.common.model.Season
import dev.polek.episodetracker.common.network.Connectivity
import dev.polek.episodetracker.common.utils.currentTimeMillis
import dev.polek.episodetracker.common.utils.formatTimestamp
import dev.polek.episodetracker.db.AddToMyShowsTask
import dev.polek.episodetracker.db.Database
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class AddToMyShowsQueue(
    private val db: Database,
    private val tmdbService: TmdbService,
    private val connectivity: Connectivity,
    private val showRepository: ShowRepository) : CoroutineScope, Connectivity.Listener {

    private val queryListener: QueryListener<AddToMyShowsTask, List<AddToMyShowsTask>>
    private val parentJob = Job()
    private val executeTaskJobs: MutableMap<Int /*showTmdbId*/, Job> = mutableMapOf()

    companion object {
        const val BACKOFF_INTERVAL_MILLIS_1 = 5_000
        const val BACKOFF_INTERVAL_MILLIS_2 = 20_000
        const val BACKOFF_INTERVAL_MILLIS_3 = 60_000
        const val BACKOFF_INTERVAL_MILLIS = 120_000
    }

    init {
        log { "Init" }

        db.addToMyShowsTaskQueries.clearProgress()

        queryListener = QueryListener(
            query = db.addToMyShowsTaskQueries.allTasks(),
            subscriber = object : QueryListener.Subscriber<List<AddToMyShowsTask>> {
                override fun onQueryResult(result: List<AddToMyShowsTask>) {
                    onTaskListModified(result)
                }
            },
            notifyImmediately = true,
            extractQueryResult = Query<AddToMyShowsTask>::executeAsList)

        connectivity.addListener(this)
    }

    override val coroutineContext: CoroutineContext
        get() {
            return ui + parentJob
        }

    override fun onConnectionAvailable() {
        log { "Connection available" }

        restartAllTasks()
    }

    override fun onConnectionLost() {
        log { "Connection lost" }
    }

    fun addShow(tmdbId: Int) {
        log { "addShow($tmdbId)" }

        val alreadyInQueue = db.addToMyShowsTaskQueries.taskExist(tmdbId).executeAsOne()
        if (alreadyInQueue) {
            loge { "Trying to add Show that's already in Queue: $tmdbId" }
            return
        }

        db.addToMyShowsTaskQueries.add(showTmdbId = tmdbId)
    }

    fun cancelAddIfExist(tmdbId: Int) {
        log { "cancelAddIfExist($tmdbId)" }

        val task = db.addToMyShowsTaskQueries.task(tmdbId).executeAsOneOrNull() ?: return

        if (task.inProgress) {
            log { "canceling Job $tmdbId" }
            executeTaskJobs[tmdbId]?.cancel()
        }

        db.addToMyShowsTaskQueries.remove(tmdbId)
    }

    private fun onTaskListModified(tasks: List<AddToMyShowsTask>) {
        log { "onTaskListModified(${tasks.joinToString { it.toLogString() }})" }

        if (tasks.isEmpty()) return

        tasks.filter { !it.inProgress }.forEach { task ->
            executeTask(task, honorBackoff = true)
        }
    }

    private fun executeTask(task: AddToMyShowsTask, honorBackoff: Boolean) {
        log { "executeTask(${task.toLogString()})" }

        val showTmdbId = task.showTmdbId

        db.addToMyShowsTaskQueries.setInProgress(showTmdbId)

        val job = launch {
            try {
                if (honorBackoff) {
                    backoffIfRequired(task)
                }

                if (!isActive) return@launch

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
                logw { "executeTask($showTmdbId) exception caught: $e" }
                db.addToMyShowsTaskQueries.setFailed(showTmdbId)
            }
        }

        executeTaskJobs[showTmdbId] = job
        job.invokeOnCompletion {
            log { "executeTask($showTmdbId) job completed" }
            executeTaskJobs.remove(showTmdbId)
        }
    }

    private suspend fun backoffIfRequired(task: AddToMyShowsTask) {
        val lastAttemptTimestampMillis = task.lastAttemptTimestampMillis ?: return
        val millisSinceLastAttempt = currentTimeMillis() - lastAttemptTimestampMillis
        val backoffInterval = when (task.retryCount) {
            1 -> BACKOFF_INTERVAL_MILLIS_1
            2 -> BACKOFF_INTERVAL_MILLIS_2
            3 -> BACKOFF_INTERVAL_MILLIS_3
            else -> BACKOFF_INTERVAL_MILLIS
        }
        val delayTime = backoffInterval - millisSinceLastAttempt

        log { "backoffIfRequired(${task.toLogString()}) delay(ms): $delayTime" }

        if (delayTime <= 0) return

        delay(delayTime)
    }

    private fun writeShowToDb(show: ShowEntity, seasons: List<Season>) {
        log { "writeShowToDb(${show.tmdbId})" }

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

    private fun restartAllTasks() {
        db.addToMyShowsTaskQueries.allTasks().executeAsList().forEach { task ->
            if (task.inProgress) {
                executeTaskJobs[task.showTmdbId]?.cancel()
            }
            executeTask(task, honorBackoff = false)
        }
    }

    private fun AddToMyShowsTask.toLogString(): String {
        val status = if (this.inProgress) "In Progress" else "Waiting"
        val lastAttempt = lastAttemptTimestampMillis?.let(::formatTimestamp).orEmpty()
        return "{Task ${this.showTmdbId}, $status, last attempt: $lastAttempt, retry count: ${this.retryCount}"
    }
}
