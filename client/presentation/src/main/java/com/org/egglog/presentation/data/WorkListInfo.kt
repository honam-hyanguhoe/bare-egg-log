package com.org.egglog.presentation.data

import com.org.egglog.domain.myCalendar.model.WorkType

data class WorkListInfo (
    val workId: Long,
    val workDate: String,
    val workType: WorkType,
)