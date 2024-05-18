package com.org.egglog.presentation.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.e("Notification", "onReceive::Notification")
        val serviceIntent = Intent(context, NotificationForegroundService::class.java).apply {
            putExtras(intent.extras!!)
        }
        context.startForegroundService(serviceIntent)
    }
}
