package com.org.egglog.presentation.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.org.egglog.presentation.R
import com.org.egglog.presentation.domain.main.activity.MainActivity

class FcmReceiveService : FirebaseMessagingService() {

    var TAG:String = "FcmReceiveService"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.from)

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.e(TAG, "Message data payload: " + remoteMessage.data)
            sendNotification(remoteMessage.data["body"])
            var sp = getSharedPreferences("Messageing", MODE_PRIVATE)
            var editor = sp.edit()
            editor.putString("SendMsg", remoteMessage.data["body"]);
            editor.putBoolean("msgSet", true)
            editor.apply()
        }

        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            Log.e(
                TAG, "Message Notification Body: " + remoteMessage.notification!!.body
            )
            sendNotification(remoteMessage.notification!!.body)
        }
        onDeletedMessages()
    }

    // [END receive_message]
    override fun onNewToken(token: String) {
        Log.e(TAG, "Refreshed token: $token")
        sendRegistrationToServer(token)
    }

    private fun handleNow() {
        Log.d(TAG, "Short lived task is done.")
        val intent = Intent(this@FcmReceiveService, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun sendRegistrationToServer(token: String) {
        // TODO: Implement this method to send token to your app server.
    }

    private fun sendNotification(messageBody: String?) {
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
        notificationChannel.vibrationPattern =
            longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
        notificationManager.createNotificationChannel(notificationChannel)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.dark)
            .setContentTitle(getString(R.string.fcm_app_name))
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .extend(
                NotificationCompat.WearableExtender()
                    .setBridgeTag("Foo")
                    .setContentIcon(R.drawable.dark)
            )
            .setContentIntent(pendingIntent)

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