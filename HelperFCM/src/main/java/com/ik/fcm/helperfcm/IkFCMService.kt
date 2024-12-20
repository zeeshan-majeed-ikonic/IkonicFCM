package com.ik.fcm.helperfcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.squareup.picasso.Picasso
import java.util.concurrent.atomic.AtomicInteger

class IkFCMService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "FCM Payload: ${remoteMessage.data}")

            val icon = remoteMessage.data["icon"]
            val title = remoteMessage.data["title"]
            val shortDesc = remoteMessage.data["short_desc"]
            val longDesc = remoteMessage.data["long_desc"]
            val image = remoteMessage.data["feature_image"]
            val packageName = remoteMessage.data["package_name"]

            if (packageName != null) {

                if (icon == null || title == null || shortDesc == null) {
                    return
                } else {
                    Handler(this.mainLooper).post {
                        sendNotification(icon, title, shortDesc, image, longDesc, packageName)
                    }
                }
            }
        }
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.d(TAG, p0)
    }

    private fun sendNotification(
        icon: String,
        title: String,
        shortDesc: String,
        image: String?,
        longDesc: String?,
        storePackage: String
    ) {
        val intent = if (!isAppInstalled(storePackage, this)) {
            setStoreIntent(storePackage)
        } else {
            openApp(storePackage)
        }


        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT
        )


        val remoteViews = RemoteViews(packageName, R.layout.firebase_notification_view)
        remoteViews.setTextViewText(R.id.tv_title, title)
        remoteViews.setTextViewText(R.id.tv_short_desc, shortDesc)

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.notification_icon) /*notification_icon*/
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .setOnlyAlertOnce(true)
            .setCustomContentView(remoteViews)
            .setCustomBigContentView(remoteViews)
            .setAutoCancel(true)

        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                getString(R.string.default_notification_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationID = getNextInt()
        notificationManager.notify(notificationID, notificationBuilder.build())


        try {
            Picasso.get().load(icon)
                .into(remoteViews, R.id.iv_icon, notificationID, notificationBuilder.build())
            if (image != null) {
                remoteViews.setViewVisibility(R.id.iv_feature, View.VISIBLE)
                Picasso.get().load(image)
                    .into(remoteViews, R.id.iv_feature, notificationID, notificationBuilder.build())
            }
        } catch (_: Exception) {
        } catch (_: java.lang.Exception) {
        } catch (_: IllegalStateException) {
        } catch (_: IllegalArgumentException) {
        }
    }

    private fun openApp(storePackage: String): Intent {
        return try {
            packageManager.getLaunchIntentForPackage(storePackage) ?: setStoreIntent(storePackage)
        } catch (e: Exception) {
            setStoreIntent(storePackage)
        }
    }

    private fun setStoreIntent(storePackage: String): Intent {
        return try {
            Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$storePackage"))
        } catch (e: ActivityNotFoundException) {
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$storePackage")
            )
        }
    }

    private fun isAppInstalled(uri: String, context: Context): Boolean {
        val pm = context.packageManager
        return try {
            val applicationInfo = pm.getApplicationInfo(uri, 0)
            applicationInfo.enabled
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    companion object {
        private val seed = AtomicInteger()
        fun getNextInt(): Int {
            return seed.incrementAndGet()
        }
    }
}