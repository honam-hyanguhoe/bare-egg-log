package com.org.egglog.data.scheduler.model

import com.org.egglog.domain.scheduler.model.AlarmFind
import kotlinx.serialization.Serializable

@Serializable
data class AlarmFindResponse (
    val workList: List<WorkListResponse>,
    val calendarGroup: CalendarGroupResponse
)

fun AlarmFindResponse.toDomainModel(): AlarmFind {
    return AlarmFind(
        workList = workList.map { it.toDomainModel() },
        calendarGroup = calendarGroup.toDomainModel()
    )
}

