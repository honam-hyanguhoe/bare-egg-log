package com.org.egglog.domain.setting.model

import java.time.LocalTime

data class WorkTypeParam(
    val title: String,
    val color: String,
    val workTypeImgUrl: String?,
    val startTime: LocalTime,
    val workTime: LocalTime
)