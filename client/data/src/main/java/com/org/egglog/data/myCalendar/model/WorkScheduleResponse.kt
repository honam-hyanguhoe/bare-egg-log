package com.org.egglog.data.myCalendar.model

import com.org.egglog.domain.myCalendar.model.WorkScheduleData
import kotlinx.serialization.Serializable

@Serializable
data class WorkScheduleResponse(
    val workList: List<WorkListResponse>,
    val calendarGroup: CalendarGroupResponse
)

fun WorkScheduleResponse.toDomainModel(): WorkScheduleData {
    return WorkScheduleData(
        workList.map { it.toDomainModel() }, calendarGroup.toDomainModel()
    )
}