package com.org.egglog.presentation.receiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import java.time.LocalTime

object AlarmConst {
    const val REQUEST_CODE = "request_code"
    const val INTERVAL_HOUR = "interval_hour"
    const val INTERVAL_MINUTE = "interval_minute"
    const val INTERVAL_SECOND = "interval_second"
    const val REPEAT_COUNT = "repeat_count"
    const val MINUTES_TO_ADD = "minutes_to_add"

    const val requestAlarmClock = 0
    const val requestDate = 1
    const val requestRepeat = 2

    const val PENDING_REPEAT_CLOCK = 0
    const val PENDING_SINGLE_DATE = 1
    const val PENDING_REPEAT = 2

    private fun getPendingIntent(context: Context, requestCode: Int, receiverIntent: Intent? = null): PendingIntent {
        val intent = receiverIntent ?: Intent(context, AlarmBroadcastReceiver::class.java)
        return PendingIntent.getBroadcast(
            context, requestCode, intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
            else
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    fun setAlarm(context: Context, alarmManager: AlarmManager, repeatCount: Int, time: LocalTime, minutesToAdd: Long) {
        val receiverIntent = Intent(context, AlarmBroadcastReceiver::class.java).apply {
            putExtra(REQUEST_CODE, requestAlarmClock)
            putExtra(INTERVAL_HOUR, time.hour)
            putExtra(INTERVAL_MINUTE, time.minute)
            putExtra(INTERVAL_SECOND, 0)
            putExtra(REPEAT_COUNT, repeatCount)
            putExtra(MINUTES_TO_ADD, minutesToAdd)
        }

        val pendingIntent = getPendingIntent(context, requestAlarmClock, receiverIntent)
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, time.hour)
            set(Calendar.MINUTE, time.minute)
            set(Calendar.SECOND, 0)
        }

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    fun cancelAlarm(context: Context, alarmManager: AlarmManager) {
        val receiverIntent = Intent(context, AlarmBroadcastReceiver::class.java)
        val pendingIntent = getPendingIntent(context, requestAlarmClock, receiverIntent)
        alarmManager.cancel(pendingIntent)
    }
}