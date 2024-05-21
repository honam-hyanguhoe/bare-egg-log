package com.org.egglog.data.myCalendar.model

import com.org.egglog.domain.myCalendar.model.CalendarGroupData
import kotlinx.serialization.Serializable

@Serializable
data class CalendarGroupResponse (
    val calendarGroupId: Int,
    val url: String?,
    val alias: String
)

fun CalendarGroupResponse.toDomainModel(): CalendarGroupData {
    return CalendarGroupData(
        calendarGroupId, url, alias
    )
}