package dev.polek.episodetracker.common.network

interface Connectivity {

    fun isConnected(): Boolean
    fun addListener(listener: Listener)

    fun removeListener(listener: Listener)

    interface Listener {
        fun onConnectionAvailable()
        fun onConnectionLost()
    }
}
