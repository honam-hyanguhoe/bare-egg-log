package com.org.egglog.domain.myCalendar.model

import kotlinx.serialization.Serializable

@Serializable
data class EditWorkData (
    val workId: Long,
    val workDate: String,
    val workTypeId: Int,
    val isDeleted: Boolean,
)