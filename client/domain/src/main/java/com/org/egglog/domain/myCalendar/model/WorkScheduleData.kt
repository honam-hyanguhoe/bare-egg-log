package com.org.egglog.domain.myCalendar.model

import kotlinx.serialization.Serializable

@Serializable
data class WorkScheduleData (
    val workList: List<WorkListData>,
    val calendarGroup: CalendarGroupData
)