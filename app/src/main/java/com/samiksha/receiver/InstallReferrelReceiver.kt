package com.samiksha.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.samiksha.utils.PreferencesManager

class InstallReferrelReceiver : BroadcastReceiver() {

    var preferencesManager: PreferencesManager? = null

    override fun onReceive(context: Context, intent: Intent) {
        val referrer = intent.getStringExtra("referrer")
        Toast.makeText(context,referrer,Toast.LENGTH_SHORT).show()
        Log.d("sassasa","sddsd")


        if (referrer!!.length == 7) {

            PreferencesManager.initializeInstance(context)
            preferencesManager = PreferencesManager.instance
            preferencesManager?.setReferalCode(referrer)
           }
    }
}