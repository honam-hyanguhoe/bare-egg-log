package com.org.egglog.data.myCalendar.model

import com.org.egglog.domain.myCalendar.model.ExcelData
import com.org.egglog.domain.myCalendar.model.ExcelIndexData
import kotlinx.serialization.Serializable

@Serializable
data class ExcelIndexResponse (
    val groupId: String,
    val date: String,
    val index: String
)

fun ExcelIndexResponse.toDomainModel(): ExcelIndexData {
    return ExcelIndexData(groupId, date, index)
}