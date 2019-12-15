package dev.polek.episodetracker.common.weakref

interface WeakRef<T: Any> {
    fun get(): T?
    fun clear()
}

expect fun <T: Any> weak(value: T): WeakRef<T>
