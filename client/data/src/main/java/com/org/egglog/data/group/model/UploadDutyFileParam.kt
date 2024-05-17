package com.org.egglog.data.group.model

import com.org.egglog.domain.group.model.DutyTag
import com.org.egglog.domain.group.model.FormattedFile
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@Serializable
class UploadDutyFileParam(
    val date: String,
    val dutyList: List<FormattedFile>,
    val customWorkTag: DutyTag,
    val day: String
) {
    fun toRequestBody(): RequestBody {
        return Json.encodeToString(this).toRequestBody()
    }

    override fun toString(): String {
        return "UploadDutyFileParam(date='$date', dutyList=$dutyList, customWorkTag=$customWorkTag, day='$day')"
    }


}