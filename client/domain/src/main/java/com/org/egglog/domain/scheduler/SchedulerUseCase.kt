package com.org.egglog.domain.scheduler

import java.time.LocalDateTime
import java.time.LocalTime

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
