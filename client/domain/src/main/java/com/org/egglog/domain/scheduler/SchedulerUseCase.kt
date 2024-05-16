package com.org.egglog.domain.scheduler

import java.time.LocalDateTime
import java.time.LocalTime

interface SchedulerUseCase {
    fun setAlarm(
        key: Int,
        curRepeatCount: Int,
        repeatCount: Int,
        time: LocalTime,
        minutesToAdd: Long,
        targetDateTime: LocalDateTime
    )

    fun cancelAllAlarms(key: Int)
}
