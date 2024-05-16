package com.org.egglog.domain.scheduler

import java.time.LocalDateTime

data class AlarmData(
    val key: Int,
    var stopByUser: Boolean,
    val repeatCount: Int,
    val curRepeatCount: Int,
    val targetDateTime: LocalDateTime,
    val minutesToAdd: Long
)
