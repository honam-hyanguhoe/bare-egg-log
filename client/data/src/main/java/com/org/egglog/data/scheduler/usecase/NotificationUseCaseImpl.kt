package com.org.egglog.data.scheduler.usecase

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import com.org.egglog.domain.scheduler.model.AlarmConst
import com.org.egglog.domain.scheduler.usecase.NotificationUseCase
import com.org.egglog.presentation.receiver.NotificationBroadcastReceiver
import java.time.LocalDateTime
import javax.inject.Inject

class NotificationUseCaseImpl @Inject constructor(
    private val context: Context,
    private val alarmManager: AlarmManager
) : NotificationUseCase {

    private fun getPendingIntent(key: Int, receiverIntent: Intent? = null): PendingIntent {
        val intent = receiverIntent ?: Intent(context, NotificationBroadcastReceiver::class.java)
        return PendingIntent.getBroadcast(
            context, key, intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    override fun setNotification(key: Int, targetDateTime: LocalDateTime) {
        val receiverIntent = Intent(context, NotificationBroadcastReceiver::class.java).apply {
            putExtra(AlarmConst.REQUEST_CODE, key)
            putExtra(AlarmConst.TARGET_DATE_YEAR, targetDateTime.year)
            putExtra(AlarmConst.TARGET_DATE_MONTH, targetDateTime.monthValue - 1)
            putExtra(AlarmConst.TARGET_DATE_DAY, targetDateTime.dayOfMonth)
            putExtra(AlarmConst.INTERVAL_HOUR, targetDateTime.hour)
            putExtra(AlarmConst.INTERVAL_MINUTE, targetDateTime.minute)
            putExtra(AlarmConst.INTERVAL_SECOND, 0)
        }

        val pendingIntent = getPendingIntent(key, receiverIntent)
        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, targetDateTime.year)
            set(Calendar.MONTH, targetDateTime.monthValue - 1)
            set(Calendar.DAY_OF_MONTH, targetDateTime.dayOfMonth)
            set(Calendar.HOUR_OF_DAY, targetDateTime.hour)
            set(Calendar.MINUTE, targetDateTime.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    override fun cancelAlarm(key: Int) {
        val pendingIntent = getPendingIntent(key)
        alarmManager.cancel(pendingIntent)
    }
}