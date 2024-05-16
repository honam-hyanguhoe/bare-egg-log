package com.org.egglog.data.scheduler.usecase

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import com.org.egglog.data.scheduler.AlarmBroadcastReceiver
import com.org.egglog.domain.scheduler.AlarmConst
import com.org.egglog.domain.scheduler.SchedulerUseCase
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

class SchedulerUseCaseImpl @Inject constructor(
    private val context: Context,
    private val alarmManager: AlarmManager
) : SchedulerUseCase {
    private var isAlarmActive = true

    private fun getPendingIntent(key: Int, receiverIntent: Intent? = null): PendingIntent {
        val intent = receiverIntent ?: Intent(context, AlarmBroadcastReceiver::class.java)
        return PendingIntent.getBroadcast(
            context, key, intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
            else
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    override fun setAlarm(
        key: Int,
        curRepeatCount: Int,
        repeatCount: Int,
        time: LocalTime,
        minutesToAdd: Long,
        targetDateTime: LocalDateTime
    ) {
        if (!isAlarmActive) return

        val receiverIntent = Intent(context, AlarmBroadcastReceiver::class.java).apply {
            putExtra(AlarmConst.REQUEST_CODE, key)
            putExtra(AlarmConst.INTERVAL_HOUR, time.hour)
            putExtra(AlarmConst.INTERVAL_MINUTE, time.minute)
            putExtra(AlarmConst.INTERVAL_SECOND, 0)
            putExtra(AlarmConst.REPEAT_COUNT, repeatCount)
            putExtra(AlarmConst.CUR_REPEAT_COUNT, curRepeatCount)
            putExtra(AlarmConst.MINUTES_TO_ADD, minutesToAdd)
            putExtra(AlarmConst.TARGET_DATE_YEAR, targetDateTime.year)
            putExtra(AlarmConst.TARGET_DATE_MONTH, targetDateTime.monthValue - 1)
            putExtra(AlarmConst.TARGET_DATE_DAY, targetDateTime.dayOfMonth)
            putExtra(AlarmConst.STOP_BY_USER, false)
        }

        val pendingIntent = getPendingIntent(key, receiverIntent)
        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, targetDateTime.year)
            set(Calendar.MONTH, targetDateTime.monthValue - 1)
            set(Calendar.DAY_OF_MONTH, targetDateTime.dayOfMonth)
            set(Calendar.HOUR_OF_DAY, time.hour)
            set(Calendar.MINUTE, time.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    override fun cancelAllAlarms(key: Int) {
        isAlarmActive = false
        val pendingIntent = getPendingIntent(key)
        alarmManager.cancel(pendingIntent)
    }
}
