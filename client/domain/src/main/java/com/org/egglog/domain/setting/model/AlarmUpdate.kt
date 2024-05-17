package com.org.egglog.domain.setting.model

import java.time.LocalTime

data class AlarmUpdate (
    val alarmId: Long,
    val alarmTime: LocalTime,
    val alarmReplayCnt: Int,
    val alarmReplayTime: Int,
    val isAlarmOn: Boolean,
    val workTypeTitle: String,
    val workTypeColor: String?,
)