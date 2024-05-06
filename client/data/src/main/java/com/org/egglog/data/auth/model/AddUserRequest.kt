package com.org.egglog.data.auth.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@Serializable
data class AddUserRequest(
    val userName: String,
    val hospitalId: Int,
    val empNo: String,
    val fcmToken: String
) {
    fun toRequestBody(): RequestBody {
        return Json.encodeToString(this).toRequestBody()
    }
}
