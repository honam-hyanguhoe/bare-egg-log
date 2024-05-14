package com.org.egglog.data.myCalendar.model

import com.org.egglog.domain.myCalendar.model.AddWorkData
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@Serializable
data class WorkScheduleParam (
    val calendarGroupId: Long,
    val workTypes: List<AddWorkData>
) {
    fun toRequestBody():RequestBody {
        return Json.encodeToString(this).toRequestBody()
    }
}