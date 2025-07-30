/*
 * Author : kiran Poman.
 * Module : Push notification module
 * Comments : This is service class which is executed by firebase SDK
 * Output : To get notification from firbase
 */
package com.samiksha.fcm

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.samiksha.R
import com.samiksha.ui.home.main.HomeActivity
import com.samiksha.ui.login.LoginActivity

import org.json.JSONException

class MyFirebaseMessagingService : FirebaseMessagingService() {
    var bitmap: Bitmap? = null
    private var title: String? = null
    private var message: String? = null
    private var notifyType: String? = null
    private var request = ""
    private var broadcaster: LocalBroadcastManager? = null
    var isLogin: String? = null
    var CHANNEL_ID = "1"
    private var id = 0

    override fun onCreate() {
        broadcaster = LocalBroadcastManager.getInstance(this)
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.e("NEW_TOKEN", p0)
    }



    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e("FCMqw", remoteMessage.data.toString())
        Log.e("FCMNoti", remoteMessage.toString())

        /* // Check if message contains a data payload.
         if (remoteMessage.data.size > 0) {
             try {
                 handleDataMessage(remoteMessage)
             } catch (e: Exception) {
                 Log.e("JSONError", e.toString())
             }
         }
         if (remoteMessage.notification != null) {
             try {
                 handleDataMessage(remoteMessage)
             } catch (e: Exception) {
                 Log.e("JSONError", e.toString())
             }
         }*/

        showChatNotification(remoteMessage)
    }

    private fun handleDataMessage(json: RemoteMessage) {

        try {


            title = json.notification!!.title
            message = json.notification!!.body

        } catch (e: JSONException) {
            Log.e(getString(R.string.app_name), "Json Exception: " + e.message)
        } catch (e: Exception) {
            Log.e(getString(R.string.app_name), "Exception: " + e.message)
        }
    }


    private fun showChatNotification(remoteMessage: RemoteMessage) {
        val title = remoteMessage.data["title"]
        val message = remoteMessage.data["body"]
        var notificationIntent: Intent? = null
        var pendingIntent: PendingIntent? = null

        val intent = Intent(applicationContext, HomeActivity::class.java)
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        //  intent.putExtra("AnotherActivity", screen);


        val builder =
            NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(message)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MAX)
        if (isLogin == "true") {
            notificationIntent = Intent(
                applicationContext,
                HomeActivity::class.java)
            notificationIntent.action = "MyRides"

            if (notificationIntent != null) {
                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    pendingIntent = PendingIntent.getActivity(
                        this,
                        0,
                        notificationIntent,
                        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT
                    )
                }else{
                    pendingIntent = PendingIntent.getActivity(
                        this,
                        0,
                        notificationIntent,
                        PendingIntent.FLAG_ONE_SHOT
                    )
                }

            }
        } else {
            notificationIntent = Intent(applicationContext, LoginActivity::class.java)
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                pendingIntent =
                    PendingIntent.getActivity(this, 0, notificationIntent,PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT)
            }else{
                pendingIntent =
                    PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT)
            }

        }
        builder.setContentIntent(pendingIntent)
        builder.setAutoCancel(true)
        sendNotification(builder)
    }


    private fun sendNotification(builder: NotificationCompat.Builder) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (notificationManager != null) {
            // Since android Oreo notification channel is needed.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    "1",
                    NotificationManager.IMPORTANCE_HIGH
                )
                notificationManager.createNotificationChannel(channel)
            }
            notificationManager.notify(
                id++ /* ID of notification */,
                builder.build()
            )
        }
    }



    fun sendLocalBroadcast(notificationId: String?) {
        val intent = Intent("notification")
        val bundle = Bundle()
        bundle.putString("title", title)
        bundle.putString("message", message)
        bundle.putString("notifyType", notifyType)
        bundle.putString("request", request)
        bundle.putString("notificationId", notificationId)
        intent.putExtras(bundle)
        broadcaster?.sendBroadcast(intent)
    }
}