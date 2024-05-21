package com.org.egglog.data.myCalendar.model

import com.org.egglog.domain.myCalendar.model.PersonalScheduleData
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