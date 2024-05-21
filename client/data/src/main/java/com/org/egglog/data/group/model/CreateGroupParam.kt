package com.org.egglog.data.group.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@Serializable
class CreateGroupParam(

    val groupName: String, val groupPassword: String, val groupImage: Int
) {
    fun toRequestBody(): RequestBody {
        return Json.encodeToString(this).toRequestBody()
    }

    override fun toString(): String {
        return "CreateGroupParam(groupName='$groupName', groupPassword='$groupPassword', groupImage=$groupImage)"
    }


}