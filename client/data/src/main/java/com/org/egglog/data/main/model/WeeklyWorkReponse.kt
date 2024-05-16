package com.org.egglog.data.main.model

import com.org.egglog.domain.main.model.WeeklyWork
import kotlinx.serialization.Serializable

@Serializable
data class WeeklyWorkResponse(
    val workList: List<WorkDTO>,
    val calendarGroup: CalendarGroupDTO
)

fun WeeklyWorkResponse.toDomainModel(): WeeklyWork {
    return WeeklyWork(
        workList = workList.mapNotNull { it.toDomainModel() },
        calendarGroup = calendarGroup.toDomainModel()
    )
}