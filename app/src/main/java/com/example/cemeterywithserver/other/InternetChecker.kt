package com.example.cemeterywithserver.other

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build


/*
    get reference to connectivityManager and check if we have capabilites for internet and things

    This functions prevents the app from crashing without wifi

    Checks if build version if marshmellow and above if so we use new way to check for capabilites otherwise
    use the old way
 */
fun checkForInternetConnection(context: Context) : Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> true
            else -> return false
        }
    }

    return connectivityManager.activeNetworkInfo?.isAvailable ?: false //case for if build version is below M

}