package com.org.egglog.data.setting.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.org.egglog.data.util.LocalTimeSerializer
import com.org.egglog.domain.setting.model.Alarm
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalTime

@Entity
@Serializable
data class AlarmResponse(
    @PrimaryKey val alarmId: Long,
    @Serializable(with = LocalTimeSerializer::class) val alarmTime: LocalTime,
    val replayCnt: Int,
    val replayTime: Int,
    val isAlarmOn: Boolean,
    val workTypeId: Long,
    val workTypeTitle: String,
    @Contextual val workTypeColor: String?,
)

fun AlarmResponse.toDomainModel(): Alarm {
    return Alarm(
        alarmId = alarmId,
        alarmTime = alarmTime,
        replayCnt = replayCnt,
        replayTime = replayTime,
        isAlarmOn = isAlarmOn,
        workTypeId = workTypeId,
        workTypeTitle = workTypeTitle,
        workTypeColor = workTypeColor
    )
}