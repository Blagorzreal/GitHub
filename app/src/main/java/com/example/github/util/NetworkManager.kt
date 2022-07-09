package com.example.github.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.lang.Exception
import java.net.InetSocketAddress
import java.net.Socket

class NetworkManager private constructor() {
    companion object {
        private const val GOOGLE_DNS = "8.8.8.8"
        private const val DNS_PORT = 53
        private const val TIMEOUT = 5000

        private val networkCallback by lazy {
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) = fireOnNetworkChanged(true)

                override fun onLost(network: Network) = fireOnNetworkChanged(false)

                private fun fireOnNetworkChanged(available: Boolean) {
                    var isOnline = available
                    if (!isOnline)
                        isOnline = isGoogleAvailableAsync()

                    if (isInternetAvailable != isOnline)
                        isInternetAvailable = isOnline
                }
            }
        }

        fun init(context: Context) {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            if (connectivityManager != null) {
                isInternetAvailable = isInternetAvailable(context)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    connectivityManager.registerDefaultNetworkCallback(networkCallback)
                else {
                    connectivityManager.registerNetworkCallback(
                        NetworkRequest.Builder()
                            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build(), networkCallback)
                }
            }
        }

        private fun isInternetAvailable(context: Context): Boolean {
            val result: Boolean

            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            if (connectivityManager == null)
                result = false
            else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val networkCapabilities = connectivityManager.activeNetwork
                    result = if (networkCapabilities == null)
                        false
                    else {
                        val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities)
                        if (actNw == null)
                            false
                        else {
                            when {
                                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                                else -> false
                            }
                        }
                    }
                } else {
                    val activeNetworkInfo = connectivityManager.activeNetworkInfo
                    result = if (activeNetworkInfo == null)
                        false
                    else {
                        when (activeNetworkInfo.type) {
                            ConnectivityManager.TYPE_WIFI -> true
                            ConnectivityManager.TYPE_MOBILE -> true
                            ConnectivityManager.TYPE_ETHERNET -> true
                            else -> false
                        }
                    }
                }
            }

            if (!result)
                return isGoogleAvailableAsync()

            return result
        }

        var isInternetAvailable = true
            get() {
                if (!field) {
                    if (isGoogleAvailableAsync())
                        field = true
                }

                return field
            }
            private set

        private fun isGoogleAvailableAsync() = runBlocking {
            val deferred = CoroutineScope(Dispatchers.IO).async {
                return@async isGoogleAvailable()
            }

            return@runBlocking try {
                deferred.await()
            } catch (e: Exception) {
                false
            }
        }

        private fun isGoogleAvailable(): Boolean {
            var socket: Socket? = null

            return try {
                socket = Socket()
                socket.connect(InetSocketAddress(GOOGLE_DNS, DNS_PORT), TIMEOUT)
                true
            } catch (error: Exception) {
                false
            } finally {
                socket?.close()
            }
        }
    }
}