package com.org.egglog.domain.myCalendar.model

import kotlinx.serialization.Serializable

@Serializable
data class WorkListData (
    val workId: Long,
    val workDate: String,
    val workType: WorkType,
)