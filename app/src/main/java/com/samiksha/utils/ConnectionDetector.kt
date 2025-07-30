/*
 * Author : Chetan Patil.
 * Module : Utils Module
 * Version : V 1.0
 * Sprint : III
 * Date of Development : 11/02/2019 5:00:00
 * Date of Modified : 11/02/2019 6:45:00
 * Comments : This class is used to detect internet
 * Output : Detect connection class
 */
package com.samiksha.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class ConnectionDetector(private val _context: Context) {
    val isConnectingToInternet: Boolean
        get() {
            val connectivity = _context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivity != null) {
                val info = connectivity.allNetworkInfo
                if (info != null) for (i in info.indices) if (info[i]
                        .state == NetworkInfo.State.CONNECTED
                ) {
                    return true
                }
            }
            return false
        }

}