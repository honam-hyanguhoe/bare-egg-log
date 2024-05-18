package com.org.egglog.domain.scheduler.model

data class AlarmFind(
    val workList: List<WorkList>,
    val calendarGroup: CalendarGroup
)