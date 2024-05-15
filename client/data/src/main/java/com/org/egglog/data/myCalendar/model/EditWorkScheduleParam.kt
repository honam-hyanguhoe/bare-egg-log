package com.org.egglog.data.myCalendar.model

import com.org.egglog.domain.myCalendar.model.EditWorkData
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@Serializable
data class EditWorkScheduleParam (
    val calendarGroupId: Long,
    val editWorkList: List<EditWorkData>
) {
    fun toRequestBody(): RequestBody {
        return Json.encodeToString(this).toRequestBody()
    }
}