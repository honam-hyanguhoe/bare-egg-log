package com.org.egglog.data.main.model

import com.org.egglog.domain.main.model.WeeklyWork
import kotlinx.serialization.Serializable

@Serializable
data class WeeklyWorkReponse(
    val workList: List<WorkDTO>,
    val calendarGroup: CalendarGroupDTO
)

fun WeeklyWorkReponse.toDomainModel(): WeeklyWork {
    return WeeklyWork(
        workList = workList.mapNotNull { it.toDomainModel() },
        calendarGroup = calendarGroup.toDomainModel()
    )
}