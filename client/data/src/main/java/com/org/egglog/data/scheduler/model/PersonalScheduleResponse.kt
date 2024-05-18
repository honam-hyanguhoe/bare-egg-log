package com.org.egglog.data.scheduler.model

import com.org.egglog.domain.scheduler.model.PersonalScheduleData
import kotlinx.serialization.Serializable

@Serializable
data class PersonalScheduleResponse (
    val date: String,
    val eventList: List<EventListResponse>,
)

fun PersonalScheduleResponse.toDomainModel(): PersonalScheduleData{
    return PersonalScheduleData(
        date, eventList.map {it.toDomainModel()}
    )
}