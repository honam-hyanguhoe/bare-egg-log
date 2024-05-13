package com.org.egglog.data.myCalendar.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import util.LocalDateTimeSerializer
import java.time.LocalDateTime

@Serializable
data class PersonalScheduleParam(
    val eventTitle: String,
    val eventContent: String,
    @Serializable(with = LocalDateTimeSerializer::class) val startDate: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class) val endDate: LocalDateTime,
    val calendarGroupId: Long
) {
    fun toRequestBody(): RequestBody {
        return Json.encodeToString(this).toRequestBody()
    }
}