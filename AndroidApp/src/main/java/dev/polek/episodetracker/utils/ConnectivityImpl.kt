package dev.polek.episodetracker.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import dev.polek.episodetracker.common.network.Connectivity
import javax.inject.Inject

class ConnectivityImpl @Inject constructor(context: Context) : Connectivity {

    private val listeners = mutableListOf<Connectivity.Listener>()
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private var isConnected: Boolean = true

    init {
        val request = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                isConnected = true
                notifyListeners()
            }

            override fun onLost(network: Network) {
                isConnected = false
                notifyListeners()
            }
        }
        connectivityManager.registerNetworkCallback(request, callback)
    }

    override fun isConnected() = isConnected

    override fun addListener(listener: Connectivity.Listener) {
        listeners.add(listener)
    }

    override fun removeListener(listener: Connectivity.Listener) {
        listeners.remove(listener)
    }

    private fun notifyListeners() {
        if (isConnected) {
            for (listener in listeners) {
                listener.onConnectionAvailable()
            }
        } else {
            for (listener in listeners) {
                listener.onConnectionLost()
            }
        }
    }
}
