package dev.polek.episodetracker.common.presentation

import dev.polek.episodetracker.common.coroutines.ui
import dev.polek.episodetracker.common.weakref.WeakRef
import dev.polek.episodetracker.common.weakref.weak
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BasePresenter<V: Any> : CoroutineScope {
    private var viewWeakRef: WeakRef<V>? = null
    var view: V?
        get() {
            return viewWeakRef?.get()
        }
        set(value) {
            viewWeakRef?.clear()
            if (value != null) {
                viewWeakRef = weak(value)
            }
        }

    private var job: Job? = null

    override val coroutineContext: CoroutineContext
        get() {
            if (job == null) {
                job = Job()
            }
            return ui + job!!
        }

    open fun attachView(view: V) {
        this.view = view
    }

    open fun detachView() {
        job?.cancel()
        job = null
        view = null
    }
}
