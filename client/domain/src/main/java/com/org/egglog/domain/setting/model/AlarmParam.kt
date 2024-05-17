package com.org.egglog.domain.setting.model

import java.time.LocalTime

data class AlarmParam(
    val alarmId: Long,
    val alarmTime: LocalTime,
    val replayCnt: Int,
    val replayTime: Int,
    val workTypeId: Long
)