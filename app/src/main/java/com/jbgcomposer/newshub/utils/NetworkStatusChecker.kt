package com.jbgcomposer.newshub.utils

import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class NetworkStatusChecker(private val connectivityManager: ConnectivityManager) {

    fun hasInternetConnection(): Boolean {
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)

    }
}