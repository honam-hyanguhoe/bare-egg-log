package com.org.egglog.presentation.receiver

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Handler
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.app.NotificationCompat
import com.org.egglog.domain.scheduler.model.AlarmConst
import com.org.egglog.domain.scheduler.model.AlarmManagerHelper
import com.org.egglog.domain.scheduler.usecase.SchedulerUseCase
import com.org.egglog.presentation.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ForegroundService : Service() {
    @Inject lateinit var schedulerUseCase: SchedulerUseCase
    private lateinit var vibrator: Vibrator
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate() {
        super.onCreate()
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val key = intent?.getIntExtra(AlarmConst.REQUEST_CODE, 0) ?: 0
        val hour = intent?.getIntExtra(AlarmConst.INTERVAL_HOUR, 0) ?: 0
        val min = intent?.getIntExtra(AlarmConst.INTERVAL_MINUTE, 0) ?: 0
        val time = "${if(hour < 10) "0$hour" else hour}:${if(min < 10) "0$min" else min}"
        startForegroundService(key, time)
        return START_STICKY
    }

    @SuppressLint("LaunchActivityFromNotification")
    private fun startForegroundService(key: Int, time: String) {
        val channelId = getString(R.string.default_notification_channel_id)
        val channelName: CharSequence = getString(R.string.default_notification_channel_name)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.BLUE
        notificationManager.createNotificationChannel(notificationChannel)

        val stopIntent = Intent(this, StopServiceReceiver::class.java).apply {
            putExtra(AlarmConst.STOP_BY_USER, true)
            putExtra(AlarmConst.REQUEST_CODE, key)
        }
        val pendingIntent = PendingIntent.getBroadcast(this, key, stopIntent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.play_store_512)
            .setContentTitle("[에그로그] $time")
            .setContentText("근무 알람이 도착했습니다!")
            .setAutoCancel(true)
            .setOngoing(true)
            .setSound(null)
            .setVibrate(null)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(key, notification)
        startVibrationAndSound()
        stopSelfInOneMinute(key)
    }

    private fun startVibrationAndSound() {
        val vibrationPattern = longArrayOf(0, 1000, 1000)
        vibrator.vibrate(VibrationEffect.createWaveform(vibrationPattern, 0))

        mediaPlayer = MediaPlayer.create(this, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)).apply {
            isLooping = true
            start()
        }
    }

    private fun stopSelfInOneMinute(key: Int) {
        Handler().postDelayed({
            stopForeground(true)
            stopSelf(key)
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
            vibrator.cancel()

            val alarmData = AlarmManagerHelper.getAlarm(key)
            if (alarmData != null) {
                if (!alarmData.stopByUser && alarmData.curRepeatCount < alarmData.repeatCount) {
                    val nextTime = alarmData.targetDateTime.plusMinutes(alarmData.minutesToAdd)
                    schedulerUseCase.setAlarm(key, alarmData.curRepeatCount + 1, alarmData.repeatCount, alarmData.minutesToAdd, nextTime, alarmData.stopByUser)
                } else {
                    schedulerUseCase.cancelAllAlarms(key)
                }
            }
        }, 60000)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        vibrator.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}

