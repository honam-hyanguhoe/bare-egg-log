package com.org.egglog.data.setting.model

import androidx.room.PrimaryKey
import com.org.egglog.data.util.LocalTimeSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.time.LocalTime

@Serializable
data class AlarmRequest(
    @PrimaryKey val alarmId: Long,
    @Serializable(with = LocalTimeSerializer::class) val alarmTime: LocalTime,
    val replayCnt: Int,
    val replayTime: Int,
    val workTypeId: Long
) {
    fun toRequestBody(): RequestBody {
        return Json.encodeToString(this).toRequestBody()
    }
}
