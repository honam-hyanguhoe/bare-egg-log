package com.org.egglog.presentation.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.org.egglog.presentation.R
import com.org.egglog.presentation.domain.main.activity.MainActivity
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class FcmReceiveService : FirebaseMessagingService() {
    var TAG:String = "FcmReceiveService"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.from)

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.e(TAG, "Message data payload: " + remoteMessage.data)
            sendNotification(remoteMessage.data["title"], remoteMessage.data["body"], remoteMessage.data["imageUrl"])
            val sp = getSharedPreferences("Messageing", MODE_PRIVATE)
            val editor = sp.edit()
            editor.putString("SendMsg", remoteMessage.data["body"]);
            editor.putBoolean("msgSet", true)
            editor.apply()
        }

        if (remoteMessage.notification != null) {
            Log.e(TAG, "Message Notification Body: " + remoteMessage.notification!!.body)
            sendNotification(remoteMessage.notification!!.title, remoteMessage.notification!!.body, remoteMessage.notification!!.imageUrl.toString())
        }
    }

    override fun onNewToken(token: String) {
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String) {

    }

    private fun getBitmapFromUrl(imageUrl: String?): Bitmap? {
        return try {
            val url = URL(imageUrl)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun sendNotification(title: String?, messageBody: String?, imageUrl: String?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        Log.e(TAG, "mesg=${messageBody}")

        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */,
            intent, PendingIntent.FLAG_IMMUTABLE
        )
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channelId = getString(R.string.default_notification_channel_id)
        val channelName: CharSequence = getString(R.string.default_notification_channel_name)
        val importance = NotificationManager.IMPORTANCE_LOW

        val notificationChannel = NotificationChannel(channelId, channelName, importance)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.BLUE
        notificationChannel.enableVibration(true)
        notificationChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
        notificationManager.createNotificationChannel(notificationChannel)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.dark)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .extend(
                NotificationCompat.WearableExtender()
                    .setBridgeTag("egglog")
                    .setContentIcon(R.drawable.dark)
            )
            .setContentIntent(pendingIntent)

        imageUrl?.let {
            val bitmap = getBitmapFromUrl(it)
            if (bitmap != null) {
                notificationBuilder.setStyle(
                    NotificationCompat.BigPictureStyle()
                        .bigPicture(bitmap)
                        .bigLargeIcon(null as Bitmap?)
                )
                notificationBuilder.setLargeIcon(bitmap)
            }
        }
        // Since android Oreo notification channel is needed.
        val channel = NotificationChannel(
            channelId,
            "Channel human readable title",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        // Dismiss notification once the user touches it.
        notificationBuilder.setAutoCancel(true)
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }
}