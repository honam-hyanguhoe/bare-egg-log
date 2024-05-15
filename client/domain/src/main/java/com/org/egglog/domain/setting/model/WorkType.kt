package com.org.egglog.domain.setting.model

data class WorkType (
    val workTypeId: Long,
    val title: String,
    val color: String,
    val workTypeImgUrl: String,
    val workTag: String,
    val startTime: String,
    val workTime: String,
)