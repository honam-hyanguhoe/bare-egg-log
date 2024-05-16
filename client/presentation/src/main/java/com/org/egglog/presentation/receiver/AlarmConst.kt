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
    const val CUR_REPEAT_COUNT = "cur_repeat_count"
    const val STOP_BY_USER = "stop_by_user"
    const val INITIAL_TIME = "initial_time"

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

    fun setDailyAlarm(context: Context, alarmManager: AlarmManager, initialTime: LocalTime) {
        val receiverIntent = Intent(context, AlarmBroadcastReceiver::class.java).apply {
            putExtra(REQUEST_CODE, requestAlarmClock)
            putExtra(INTERVAL_HOUR, initialTime.hour)
            putExtra(INTERVAL_MINUTE, initialTime.minute)
            putExtra(INTERVAL_SECOND, 0)
            putExtra(REPEAT_COUNT, 0)
            putExtra(CUR_REPEAT_COUNT, 0)
            putExtra(MINUTES_TO_ADD, 0L)
            putExtra(STOP_BY_USER, false)
            putExtra(INITIAL_TIME, initialTime.toString())
        }

        val pendingIntent = getPendingIntent(context, requestAlarmClock, receiverIntent)
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, initialTime.hour)
            set(Calendar.MINUTE, initialTime.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    fun setOneTimeAlarm(context: Context, alarmManager: AlarmManager, curRepeatCount: Int, repeatCount: Int, initialTime: LocalTime, minutesToAdd: Long) {
        val nextTime = initialTime.plusMinutes(minutesToAdd * (curRepeatCount + 1))
        val receiverIntent = Intent(context, AlarmBroadcastReceiver::class.java).apply {
            putExtra(REQUEST_CODE, requestAlarmClock)
            putExtra(INTERVAL_HOUR, nextTime.hour)
            putExtra(INTERVAL_MINUTE, nextTime.minute)
            putExtra(INTERVAL_SECOND, 0)
            putExtra(REPEAT_COUNT, repeatCount)
            putExtra(CUR_REPEAT_COUNT, curRepeatCount)
            putExtra(MINUTES_TO_ADD, minutesToAdd)
            putExtra(STOP_BY_USER, false)
            putExtra(INITIAL_TIME, initialTime.toString())
        }

        val pendingIntent = getPendingIntent(context, requestAlarmClock, receiverIntent)
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, nextTime.hour)
            set(Calendar.MINUTE, nextTime.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    fun cancelAllAlarms(context: Context, alarmManager: AlarmManager) {
        val pendingIntent = getPendingIntent(context, requestAlarmClock)
        alarmManager.cancel(pendingIntent)
    }
}