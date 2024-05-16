package com.org.egglog.data.group.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@Serializable
class InvitationParam(
    val invitationCode : String,
    val password : String,
) {
    fun toRequestBody(): RequestBody {
        return Json.encodeToString(this).toRequestBody()
    }

    override fun toString(): String {
        return "InvitationParam(invitationCode='$invitationCode', password='$password')"
    }


}