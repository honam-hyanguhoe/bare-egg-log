package com.org.egglog.presentation.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import androidx.core.app.NotificationCompat
import com.org.egglog.domain.scheduler.usecase.NotificationUseCase
import com.org.egglog.presentation.R
import com.org.egglog.presentation.domain.myCalendar.activity.MyCalendarActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotificationBroadcastReceiver : BroadcastReceiver() {
    @Inject lateinit var notificationManager: NotificationManager
    @Inject lateinit var notificationUseCase: NotificationUseCase

    override fun onReceive(context: Context, intent: Intent) {
        Log.e("Notification", "onReceive::Notification")
        val key = intent.getIntExtra("REQUEST_CODE", 0)
        val title = "[에그로그]"
        val message = "30분 뒤 일정이 있습니다!\n확인하러 GoGo!"

        createNotificationChannel(context)
        sendNotification(context, key, title, message)
        notificationUseCase.cancelAlarm(key)
    }

    private fun createNotificationChannel(context: Context) {
        val channelId = context.getString(R.string.default_notification_channel_id)
        val channelName = context.getString(R.string.default_notification_channel_name)
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH).apply {
            enableLights(true)
            lightColor = Color.BLUE
            enableVibration(true)
            description = "Notification channel description"
        }

        notificationManager.createNotificationChannel(channel)
    }

    private fun sendNotification(context: Context, key: Int, title: String, message: String) {
        val channelId = context.getString(R.string.default_notification_channel_id)

        val mainIntent = Intent(context, MyCalendarActivity::class.java)
        val mainPendingIntent = PendingIntent.getActivity(
            context, key, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.play_store_512)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(mainPendingIntent)
            .build()

        notificationManager.notify(key, notification)
    }
}
