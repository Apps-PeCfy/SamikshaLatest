package com.samiksha.fcm

import android.app.DownloadManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder

class DownloadBroadcastReceiverService : Service() {
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        registerDownloadCompleteReceiver()
    }

    override fun onDestroy() {
        unregisterReceiver(onCompleteDownload)
    }

    private fun registerDownloadCompleteReceiver() {
        onCompleteDownload = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {

            }
        }
        val filter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        registerReceiver(onCompleteDownload, filter)
    }

    companion object {
        private var onCompleteDownload: BroadcastReceiver? = null
    }
}