package com.org.egglog.domain.main.model

import kotlinx.serialization.Serializable

@Serializable
data class CalendarGroup (
    val calendarGroupId : Int,
    val url : String,
    val alias : String
)