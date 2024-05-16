package com.org.egglog.presentation.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.e("AlarmTest", "onReceive:: REQUEST_ALARM_CLOCK")
        val serviceIntent = Intent(context, ForegroundService::class.java).apply {
            putExtras(intent.extras!!)
        }
        context.startForegroundService(serviceIntent)
    }
}