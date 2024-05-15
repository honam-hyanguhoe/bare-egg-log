package com.org.egglog.presentation.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.extras?.getInt(AlarmConst.REQUEST_CODE)) {
            AlarmConst.requestAlarmClock -> {
                Log.e("AlarmTest", "onReceive:: REQUEST_ALARM_CLOCK")

                val serviceIntent = Intent(context, ForegroundService::class.java).apply {
                    putExtras(intent.extras!!) // 전달된 모든 extras 포함
                }
                context.startForegroundService(serviceIntent)
            }
        }
    }
}