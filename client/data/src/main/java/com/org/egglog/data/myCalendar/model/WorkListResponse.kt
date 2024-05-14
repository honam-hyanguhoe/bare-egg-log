package com.org.egglog.data.myCalendar.model

import kotlinx.serialization.Serializable

@Serializable
data class WorkListResponse (
    val workId: Long,
    val workDate: String,
    val workType: WorkTypeResponse
)