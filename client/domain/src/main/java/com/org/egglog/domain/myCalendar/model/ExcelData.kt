package com.org.egglog.domain.myCalendar.model

import kotlinx.serialization.Serializable

@Serializable
data class ExcelData (
    val date: String,
    val userName: String,
    val index: ExcelIndexData
)