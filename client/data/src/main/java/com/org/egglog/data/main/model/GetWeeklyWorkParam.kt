package com.org.egglog.data.main.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@Serializable
data class GetWeeklyWorkParam(
    val startDate: String,
    val endDate: String
) {
    fun toRequestBody(): RequestBody {
        return Json.encodeToString(this).toRequestBody()
    }

    override fun toString(): String {
        return "GetWeeklyWorkParam(startDate='$startDate', endDate='$endDate')"
    }

}
