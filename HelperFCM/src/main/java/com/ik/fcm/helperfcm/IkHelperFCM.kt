package com.ik.fcm.helperfcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.runBlocking

object IkHelperFCM {
    fun startFCMService(context: Context, topic: String) {
        runBlocking {
            initializeFirebaseFCM(context)
            createChannelForFCM(context)
            FirebaseMessaging.getInstance().subscribeToTopic(topic)
            Log.i(TAG, "FCM Service started")
        }
    }

    fun stopFCMService(topic: String) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
    }

    private fun initializeFirebaseFCM(context: Context) {
        try {
            FirebaseApp.initializeApp(context)
            Log.i(TAG, "Firebase FCM Initialized")
        } catch (e: Exception) {
            Log.e(TAG, "Exception during initialization: ${e.message}")
        }
    }

    private fun createChannelForFCM(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = context.getString(R.string.default_notification_channel_id)
            val channelName = context.getString(R.string.default_notification_channel_id)
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    channelId,
                    channelName, NotificationManager.IMPORTANCE_DEFAULT
                )
            )
            Log.i(TAG, "FCM Channel created")
        }
    }
}