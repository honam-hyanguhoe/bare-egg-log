package com.org.egglog.presentation.receiver

import android.annotation.SuppressLint
import android.app.AlarmManager
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
import com.org.egglog.presentation.R
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class ForegroundService : Service() {
    private lateinit var vibrator: Vibrator
    private var mediaPlayer: MediaPlayer? = null
    private var stopByUser = false

    override fun onCreate() {
        super.onCreate()
        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val repeatCount = intent?.getIntExtra(AlarmConst.REPEAT_COUNT, 0) ?: 0
        val curRepeatCount = intent?.getIntExtra(AlarmConst.CUR_REPEAT_COUNT, repeatCount) ?: repeatCount
        val initialTime = LocalTime.parse(intent?.getStringExtra(AlarmConst.INITIAL_TIME)) ?: LocalTime.of(0, 0)
        val minutesToAdd = intent?.getLongExtra(AlarmConst.MINUTES_TO_ADD, 5) ?: 5L
        stopByUser = intent?.getBooleanExtra(AlarmConst.STOP_BY_USER, false) ?: false

        startForegroundService(curRepeatCount = curRepeatCount, message = LocalTime.now().format(formatter), repeatCount = repeatCount, initialTime = initialTime, minutesToAdd = minutesToAdd, stopByUser = stopByUser)
        return START_STICKY
    }

    @SuppressLint("LaunchActivityFromNotification")
    private fun startForegroundService(title: String = "에그로그 알림", message: String, curRepeatCount: Int, repeatCount: Int, initialTime: LocalTime, minutesToAdd: Long, stopByUser: Boolean) {
        val channelId = getString(R.string.default_notification_channel_id)
        val channelName: CharSequence = getString(R.string.default_notification_channel_name)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.BLUE
        notificationManager.createNotificationChannel(notificationChannel)

        val stopIntent = Intent(this, StopServiceReceiver::class.java).apply {
            putExtra(AlarmConst.STOP_BY_USER, true)
            putExtra(AlarmConst.INITIAL_TIME, initialTime.toString())
        }
        val pendingIntent = PendingIntent.getBroadcast(this, 0, stopIntent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.dark)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setOngoing(true)
            .setSound(null)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1, notification)
        startVibrationAndSound()
        stopSelfInOneMinute(curRepeatCount, repeatCount, initialTime, minutesToAdd)
    }

    private fun startVibrationAndSound() {
        val vibrationPattern = longArrayOf(0, 1000, 1000)
        vibrator.vibrate(VibrationEffect.createWaveform(vibrationPattern, 0))

        mediaPlayer = MediaPlayer.create(this, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)).apply {
            isLooping = true
            start()
        }
    }

    private fun stopSelfInOneMinute(curRepeatCount: Int, repeatCount: Int, initialTime: LocalTime, minutesToAdd: Long) {
        val handler = Handler(mainLooper)
        handler.postDelayed({
            stopSelf()
            if (!stopByUser && curRepeatCount < repeatCount) {
                val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                AlarmConst.setOneTimeAlarm(context = this, alarmManager = alarmManager, repeatCount = repeatCount, initialTime = initialTime, curRepeatCount = curRepeatCount + 1, minutesToAdd = minutesToAdd)
            }
        }, 60000) // 60000 ms == 1 minute
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        vibrator.cancel()
        mediaPlayer?.stop()
        mediaPlayer?.release()
    }
}