package com.org.egglog.domain.myCalendar.model

import kotlinx.serialization.Serializable

@Serializable
data class ExcelIndexData (
    val groupId: String,
    val date: String,
    val index: String
)