package fbo.costa.vagalumelyrics.util.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NetworkListener : ConnectivityManager.NetworkCallback() {

    private val isNetworkAvailable = MutableStateFlow(false)

    fun checkNetworkAvailability(context: Context): MutableStateFlow<Boolean> {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(this)

        var hasNetwork = false

        connectivityManager.allNetworks.forEach { _network ->
            val networkCapabilities = connectivityManager.getNetworkCapabilities(_network)
            networkCapabilities?.let { _networkCapabilities ->
                if (_networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                    hasNetwork = true
                    return@forEach
                }
            }
        }

        isNetworkAvailable.value = hasNetwork

        return isNetworkAvailable
    }

    override fun onAvailable(network: Network) {
        isNetworkAvailable.value = true

        // Network available, but now check if has Internet
        // This may take a few seconds
        CoroutineScope(Dispatchers.IO).launch {
            val hasInternet = HasInternet.execute()
            withContext(Dispatchers.Main) {
                isNetworkAvailable.value = hasInternet
            }
        }
    }

    override fun onLost(network: Network) {
        isNetworkAvailable.value = false
    }
}
