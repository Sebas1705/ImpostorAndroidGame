package es.sebas1705.domain.managers

import android.Manifest
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.annotation.RequiresPermission
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manager to handle network connectivity checks and callbacks.
 *
 * @property connectivityManager The system's connectivity manager to check network status.
 *
 * @since 0.1.0
 * @author Sebas1705 03/07/2025
 */
@Singleton
class ConnectivityManager @Inject constructor(
    private val connectivityManager: ConnectivityManager
) {

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun isNetworkAvailable(): Boolean {
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun registerNetworkCallback(callback: ConnectivityManager.NetworkCallback) {
        connectivityManager.registerDefaultNetworkCallback(callback)
    }

    fun unregisterNetworkCallback(callback: ConnectivityManager.NetworkCallback) {
        connectivityManager.unregisterNetworkCallback(callback)
    }
}