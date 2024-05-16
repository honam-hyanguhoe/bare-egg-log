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
class AlarmBroadcastReceiver : BroadcastReceiver() {
    @Inject lateinit var schedulerUseCase: SchedulerUseCase

    override fun onReceive(context: Context, intent: Intent) {
        Log.e("AlarmTest", "onReceive:: REQUEST_ALARM_CLOCK")
        val key = intent.getIntExtra(AlarmConst.REQUEST_CODE, 0)
        val stopByUser = intent.getBooleanExtra(AlarmConst.STOP_BY_USER, false)

        if (stopByUser) {
            schedulerUseCase.cancelAllAlarms(key)
        } else {
            val serviceIntent = Intent(context, ForegroundService::class.java).apply {
                putExtras(intent.extras!!)
            }
            context.startForegroundService(serviceIntent)
        }
    }
}
