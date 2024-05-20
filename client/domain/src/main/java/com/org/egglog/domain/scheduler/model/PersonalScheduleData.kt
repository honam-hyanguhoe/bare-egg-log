package com.org.egglog.domain.scheduler.model

import kotlinx.serialization.Serializable

@Serializable
data class PersonalScheduleData (
    val date: String,
    val eventList: List<EventListData>,
)