package dev.polek.episodetracker.common.datasource.db

import co.touchlab.stately.concurrency.ThreadLocalRef
import co.touchlab.stately.freeze
import com.squareup.sqldelight.Query
import dev.polek.episodetracker.common.datasource.db.QueryListener.Subscriber

class MultiQueryListener<Q : Any, R>(
    private val queries: List<Query<Q>>,
    subscriber: Subscriber<List<R>>,
    notifyImmediately: Boolean,
    private inline val extractQueryResult: (Query<Q>) -> List<R>) : Query.Listener
{
    private val subscriberRef = ThreadLocalRef<Subscriber<List<R>>>()

    init {
        subscriberRef.set(subscriber)
        queries.forEach { it.addListener(this) }
        freeze()

        if (notifyImmediately) {
            queryResultsChanged()
        }
    }

    fun destroy() {
        queries.forEach { it.removeListener(this) }
        subscriberRef.set(null)
    }

    override fun queryResultsChanged() {
        val result = queries.map(extractQueryResult).flatten()
        subscriberRef.get()?.onQueryResult(result)
    }
}
