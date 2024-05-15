package com.org.egglog.presentation.receiver

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class StopServiceReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val stopIntent = Intent(context, ForegroundService::class.java)
        context.stopService(stopIntent)
//        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        AlarmConst.cancelAlarm(context, alarmManager)
    }
}
