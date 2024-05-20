package com.org.egglog.domain.scheduler.model

import java.time.LocalTime

data class Alarm (
    val alarmId: Long,
    val alarmTime: LocalTime,
    val alarmReplayCnt: Int,
    val alarmReplayTime: Int,
    val isAlarmOn: Boolean,
    val workTypeTitle: String?,
    val workTypeColor: String?
)