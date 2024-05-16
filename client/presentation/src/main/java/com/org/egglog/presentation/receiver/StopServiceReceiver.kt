package com.org.egglog.presentation.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.org.egglog.domain.scheduler.AlarmConst
import com.org.egglog.domain.scheduler.SchedulerUseCase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StopServiceReceiver : BroadcastReceiver() {
    @Inject lateinit var schedulerUseCase: SchedulerUseCase

    override fun onReceive(context: Context, intent: Intent?) {
        val key = intent?.getIntExtra(AlarmConst.REQUEST_CODE, 0) ?: 0
        val stopIntent = Intent(context, ForegroundService::class.java).apply {
            putExtra(AlarmConst.STOP_BY_USER, true)
        }
        context.stopService(stopIntent)
        schedulerUseCase.cancelAllAlarms(key)
        Log.e("AlarmTest", "All alarms have been canceled.")
    }
}