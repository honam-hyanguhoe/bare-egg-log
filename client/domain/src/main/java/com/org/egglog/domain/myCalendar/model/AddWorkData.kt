package com.org.egglog.domain.myCalendar.model

import kotlinx.serialization.Serializable

@Serializable
data class AddWorkData (
    val workDate: String,
    val workTypeId: Int
)