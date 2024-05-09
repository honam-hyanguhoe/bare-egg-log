package com.org.egglog.data.community.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@Serializable
data class CreateCommentRequestParam (
    val boardId: Int,
    val commentContent: String,
    val parentId: Long,
    val tempNickname: String
) {
    fun toRequestBody(): RequestBody {
        return Json.encodeToString(this).toRequestBody()
    }
}