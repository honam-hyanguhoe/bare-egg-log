package com.org.egglog.data.setting.model

import com.org.egglog.data.util.LocalTimeSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.time.LocalTime

@Serializable
data class WorkTypeModifyRequest(
    val workTypeId: Long,
    val title: String,
    val color: String,
    val workTypeImgUrl: String?,
    @Serializable(with = LocalTimeSerializer::class) val startTime: LocalTime,
    @Serializable(with = LocalTimeSerializer::class) val workTime: LocalTime
) {
    fun toRequestBody(): RequestBody {
        return Json.encodeToString(this).toRequestBody()
    }
}
