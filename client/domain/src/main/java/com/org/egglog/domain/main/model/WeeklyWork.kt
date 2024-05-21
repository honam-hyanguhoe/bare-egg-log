package com.org.egglog.domain.main.model

import kotlinx.serialization.Serializable

@Serializable
data class WeeklyWork(
    val workList : List<Work>,
    val calendarGroup : CalendarGroup
)

