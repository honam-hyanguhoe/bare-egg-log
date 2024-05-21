package com.org.egglog.domain.myCalendar.model

import kotlinx.serialization.Serializable

@Serializable
data class EventListData (
    val eventId: Int,
    val eventTitle: String,
    val eventContent: String ?= "",
    val startDate: String,
    val endDate: String,
    val calendarGroupId: Long
)