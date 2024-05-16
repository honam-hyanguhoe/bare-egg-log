package com.org.egglog.domain.myCalendar.model

import kotlinx.serialization.Serializable

@Serializable
data class CalendarGroupData (
    val calendarGroupId: Int,
    val url: String?,
    val alias: String,
)