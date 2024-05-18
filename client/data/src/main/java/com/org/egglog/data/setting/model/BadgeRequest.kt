package com.org.egglog.data.setting.model

import com.org.egglog.data.util.LocalTimeSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.time.LocalTime

@Serializable
data class BadgeRequest(
    val nurseCertificationImgUrl: String,
    val hospitalCertificationImgUrl: String
) {
    fun toRequestBody(): RequestBody {
        return Json.encodeToString(this).toRequestBody()
    }
}
