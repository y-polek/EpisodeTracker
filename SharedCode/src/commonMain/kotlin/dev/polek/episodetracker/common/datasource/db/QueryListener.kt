package dev.polek.episodetracker.common.datasource.db

import co.touchlab.stately.concurrency.ThreadLocalRef
import co.touchlab.stately.freeze
import com.squareup.sqldelight.Query

class QueryListener<Q : Any, R>(
    private val query: Query<Q>,
    subscriber: Subscriber<R>,
    notifyImmediately: Boolean = false,
    private inline val extractQueryResult: (Query<Q>) -> R) : Query.Listener
{
    private val subscriberRef = ThreadLocalRef<Subscriber<R>>()

    init {
        subscriberRef.set(subscriber)
        query.addListener(this)
        freeze()

        if (notifyImmediately) {
            queryResultsChanged()
        }
    }

    fun destroy() {
        query.removeListener(this)
        subscriberRef.set(null)
    }

    override fun queryResultsChanged() {
        val result = extractQueryResult(query)
        subscriberRef.get()?.onQueryResult(result)
    }
}

interface Subscriber<T> {
    fun onQueryResult(result: T)
}
