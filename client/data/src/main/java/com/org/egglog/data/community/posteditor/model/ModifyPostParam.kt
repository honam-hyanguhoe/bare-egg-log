package com.org.egglog.data.community.posteditor.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@Serializable
data class ModifyPostParam (
    val boardTitle : String,
    val boardContent : String,
    val pictureOne : String,
    val pictureTwo : String,
    val pictureThree : String,
    val pictureFour : String,
) {
    fun toRequestBody() : RequestBody {
        return Json.encodeToString(this).toRequestBody()
    }
}