package fbo.costa.vagalumelyrics.util

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.LiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkLiveData
@Inject
constructor(
    context: Application
) : LiveData<Boolean>() {

    private var _network: Network? = null
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    private val cnn = context.getSystemService(Context.CONNECTIVITY_SERVICE)
            as ConnectivityManager

    private val validNetworks: MutableSet<Network> = HashSet()

    private fun checkValidNetworks() {
        postValue(validNetworks.size > 0)
    }

    override fun onActive() {
        networkCallback = createNetworkCallback()
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        cnn.registerNetworkCallback(networkRequest, networkCallback)
    }

    override fun onInactive() {
        cnn.unregisterNetworkCallback(networkCallback)
    }

    private fun createNetworkCallback() = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            _network = network

            if (isOnline()) validNetworks.add(network)
            checkValidNetworks()
        }

        override fun onLost(network: Network) {
            validNetworks.remove(network)
            checkValidNetworks()
        }
    }

    fun isOnline(): Boolean {
        val network = when (_network) {
            null -> cnn.activeNetwork
            else -> _network
        }

        network ?: return false
        val networkCapabilities = cnn.getNetworkCapabilities(network) ?: return false

        return when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    }
}
