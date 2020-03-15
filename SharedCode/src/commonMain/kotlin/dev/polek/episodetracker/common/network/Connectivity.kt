package dev.polek.episodetracker.common.network

abstract class Connectivity {

    private val listeners = mutableListOf<Listener>()

    abstract fun isConnected(): Boolean

    fun addListener(listener: Listener) {
        listeners.add(listener)
    }

    fun removeListener(listener: Listener) {
        listeners.remove(listener)
    }

    protected fun notifyConnectionAvailable() {
        listeners.forEach { it.onConnectionAvailable() }
    }

    protected fun notifyConnectionLost() {
        listeners.forEach { it.onConnectionLost() }
    }

    interface Listener {
        fun onConnectionAvailable()
        fun onConnectionLost()
    }
}
