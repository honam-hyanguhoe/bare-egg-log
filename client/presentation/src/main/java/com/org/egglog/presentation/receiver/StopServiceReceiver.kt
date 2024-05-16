package com.org.egglog.presentation.receiver

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class StopServiceReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val stopIntent = Intent(context, ForegroundService::class.java).apply {
            putExtra(AlarmConst.STOP_BY_USER, true)
        }
        context.stopService(stopIntent)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        AlarmConst.cancelAllAlarms(context, alarmManager)
        Log.e("AlarmTest", "All alarms have been canceled.")
    }
}