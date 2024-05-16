package com.org.egglog.data.group.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@Serializable
class MembersWorkParam(
    val userGroupId: Long, val targetUserId: Long, val startDate: String, val endDate: String
) {
    fun toRequestBody(): RequestBody {
        return Json.encodeToString(this).toRequestBody()
    }

    override fun toString(): String {
        return "MembersWorkParam(userGroupId=$userGroupId, targetUserId=$targetUserId, startDate='$startDate', endDate='$endDate')"
    }
}