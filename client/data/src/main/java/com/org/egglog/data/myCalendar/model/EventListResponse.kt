package com.org.egglog.data.myCalendar.model

import com.org.egglog.domain.myCalendar.model.EventListData
import kotlinx.serialization.Serializable

@Serializable
data class EventListResponse (
    val eventId: Int,
    val eventTitle: String,
    val eventContent: String ?= "",
    val startDate: String,
    val endDate: String,
    val calendarGroupId: Long
)

fun EventListResponse.toDomainModel(): EventListData {
    return EventListData(eventId, eventTitle, eventContent, startDate, endDate, calendarGroupId)
}