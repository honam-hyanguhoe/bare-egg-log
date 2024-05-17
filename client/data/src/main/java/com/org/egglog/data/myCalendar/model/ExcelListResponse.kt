package com.org.egglog.data.myCalendar.model

import com.org.egglog.domain.myCalendar.model.ExcelData
import kotlinx.serialization.Serializable

@Serializable
data class ExcelListResponse (
    val date: String,
    val userName: String,
    val index: ExcelIndexResponse
)

fun ExcelListResponse.toDomainModel(): ExcelData {
    return ExcelData(date, userName, index.toDomainModel())
}