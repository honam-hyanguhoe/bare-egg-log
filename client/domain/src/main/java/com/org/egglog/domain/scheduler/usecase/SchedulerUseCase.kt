package com.org.egglog.domain.scheduler.usecase

import java.time.LocalDateTime

interface SchedulerUseCase {
    fun setAlarm(
        key: Int,
        curRepeatCount: Int,
        repeatCount: Int,
        minutesToAdd: Long,
        targetDateTime: LocalDateTime,
        stopByUser: Boolean
    )

    fun cancelAllAlarms(key: Int)
}
