package dev.polek.episodetracker.common.weakref

import java.lang.ref.WeakReference

internal class AndroidWeakRef<T: Any>(private val weakValue: WeakReference<T>): WeakRef<T> {

    override fun get(): T? {
        return weakValue.get()
    }

    override fun clear() {
        weakValue.clear()
    }
}

actual fun <T: Any> weak(value: T): WeakRef<T> {
    return AndroidWeakRef(WeakReference(value))
}
