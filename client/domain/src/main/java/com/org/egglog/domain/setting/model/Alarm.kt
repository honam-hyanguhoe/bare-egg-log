package com.org.egglog.domain.setting.model

import java.time.LocalTime

data class Alarm (
    val alarmId: Long,
    val alarmTime: LocalTime,
    val replayCnt: Int,
    val replayTime: Int,
    val isAlarmOn: Boolean,
    val workTypeId: Long,
    val workTypeTitle: String,
    val workTypeColor: String?,
)