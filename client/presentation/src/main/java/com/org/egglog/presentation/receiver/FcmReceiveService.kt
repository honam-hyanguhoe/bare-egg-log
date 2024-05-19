package com.org.egglog.presentation.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.org.egglog.domain.auth.model.UserFcmTokenParam
import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.domain.auth.usecase.SetUserStoreUseCase
import com.org.egglog.domain.auth.usecase.UpdateUserFcmTokenUseCase
import com.org.egglog.presentation.R
import com.org.egglog.presentation.domain.main.activity.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject


@AndroidEntryPoint
class FcmReceiveService : FirebaseMessagingService() {
    @Inject lateinit var updateUserFcmTokenUseCase: UpdateUserFcmTokenUseCase
    @Inject lateinit var getTokenStoreUseCase: GetTokenUseCase
    @Inject lateinit var setUserStoreUseCase: SetUserStoreUseCase
    var TAG:String = "FcmReceiveService"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.from)

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.e(TAG, "Message data payload: " + remoteMessage.data)
            sendNotification(remoteMessage.data["title"], remoteMessage.data["body"], remoteMessage.data["imageUrl"], remoteMessage.data["click_action"])
            val sp = getSharedPreferences("Messageing", MODE_PRIVATE)
            val editor = sp.edit()
            editor.putString("SendMsg", remoteMessage.data["body"]);
            editor.putBoolean("msgSet", true)
            editor.apply()
        }

        if (remoteMessage.notification != null) {
            Log.e(TAG, "Message Notification Body: " + remoteMessage.notification!!.body)
            sendNotification(remoteMessage.notification!!.title, remoteMessage.notification!!.body, remoteMessage.notification!!.imageUrl.toString(), remoteMessage.notification!!.clickAction.toString())
        }
    }

    override fun onNewToken(token: String) {
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(deviceToken: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val tokens = getTokenStoreUseCase()
                val newUser = updateUserFcmTokenUseCase("Bearer ${tokens.first.orEmpty()}", UserFcmTokenParam(deviceToken)).getOrThrow()
                setUserStoreUseCase(newUser)
            } catch (e: Exception) {
                Log.e(TAG, "Error while sending registration to server", e)
            }
        }
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

    private fun sendNotification(title: String?, messageBody: String?, imageUrl: String?, clickAction:String?) {
        val intent = if (!clickAction.isNullOrEmpty()) {
            Intent(Intent.ACTION_VIEW, Uri.parse(clickAction)).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
        } else {
            Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
        }

        Log.d("deep", "deep ${clickAction}")
        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */,
            intent, PendingIntent.FLAG_IMMUTABLE
        )
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channelId = getString(R.string.default_notification_channel_id)
        val channelName: CharSequence = getString(R.string.default_notification_channel_name)
        val importance = NotificationManager.IMPORTANCE_HIGH

        val notificationChannel = NotificationChannel(channelId, channelName, importance)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.BLUE
        notificationChannel.enableVibration(true)
        notificationChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
        notificationManager.createNotificationChannel(notificationChannel)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.play_store_512)
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
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)

        // Dismiss notification once the user touches it.
        notificationBuilder.setAutoCancel(true)
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }
}