package com.org.egglog.domain.myCalendar.model

import kotlinx.serialization.Serializable

@Serializable
data class PersonalScheduleData (
    val date: String,
    val eventList: List<EventListData>,
)