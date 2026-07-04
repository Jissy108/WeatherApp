package com.example.weatherapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

object Constants {
     const val APP_ID = "b4f52e3dcaf97400f6f9f9621426ea3c"
     const val  BASE_URL = "https://api.openweathermap.org/"

     const val METRIC_UNIT = "metric"
    fun isNetworkAvailable(context: Context): Boolean {

        val connectivityManager =
            context.getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            val network =
                connectivityManager.activeNetwork ?: return false

            val activeNetwork =
                connectivityManager.getNetworkCapabilities(network)
                    ?: return false

            return when {

                activeNetwork.hasTransport(
                    NetworkCapabilities.TRANSPORT_WIFI
                ) -> true

                activeNetwork.hasTransport(
                    NetworkCapabilities.TRANSPORT_CELLULAR
                ) -> true

                activeNetwork.hasTransport(
                    NetworkCapabilities.TRANSPORT_ETHERNET
                ) -> true

                else -> false
            }

        } else {

            val networkInfo =
                connectivityManager.activeNetworkInfo

            return networkInfo != null &&
                    networkInfo.isConnectedOrConnecting
        }
    }
}