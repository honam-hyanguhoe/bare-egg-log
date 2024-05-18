package com.org.egglog.presentation.receiver

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.org.egglog.domain.scheduler.model.AlarmConst
import com.org.egglog.domain.scheduler.usecase.NotificationUseCase
import com.org.egglog.presentation.R
import com.org.egglog.presentation.domain.myCalendar.activity.MyCalendarActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotificationForegroundService : Service() {
    @Inject lateinit var notificationUseCase: NotificationUseCase
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val key = intent?.getIntExtra(AlarmConst.REQUEST_CODE, 0) ?: 0
        startForegroundService(key)
        return START_STICKY
    }

    @SuppressLint("LaunchActivityFromNotification")
    private fun startForegroundService(key: Int) {
        val channelId = getString(R.string.default_notification_channel_id)
        val channelName: CharSequence = getString(R.string.default_notification_channel_name)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.BLUE
        notificationManager.createNotificationChannel(notificationChannel)

        val clickIntent = Intent(this, MyCalendarActivity::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, key, clickIntent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.play_store_512)
            .setContentTitle("[에그로그]")
            .setContentText("개인 일정 알람!")
            .setAutoCancel(true)
            .setOngoing(true)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(key, notification)
        notificationUseCase.cancelAlarm(key)
        stopSelf()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}

