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
import com.org.egglog.domain.scheduler.AlarmConst
import com.org.egglog.domain.scheduler.AlarmData
import com.org.egglog.domain.scheduler.AlarmManagerHelper
import com.org.egglog.domain.scheduler.SchedulerUseCase
import com.org.egglog.presentation.R
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
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

        val stopIntent = Intent(this, StopServiceReceiver::class.java).apply {
            putExtra(AlarmConst.STOP_BY_USER, true)
            putExtra(AlarmConst.REQUEST_CODE, key)
        }
        val pendingIntent = PendingIntent.getBroadcast(this, key, stopIntent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.dark)
            .setContentTitle("에그로그 알림")
            .setContentText("반복 알람 실행 중...")
            .setAutoCancel(true)
            .setOngoing(true)
            .setSound(null)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1, notification)
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
            stopSelf()
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
