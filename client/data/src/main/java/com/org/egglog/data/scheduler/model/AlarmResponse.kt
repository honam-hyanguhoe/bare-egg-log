package com.org.egglog.data.scheduler.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.org.egglog.data.util.LocalTimeSerializer
import com.org.egglog.domain.scheduler.model.Alarm
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalTime

@Entity
@Serializable
data class AlarmResponse(
    @PrimaryKey val alarmId: Long,
    @Serializable(with = LocalTimeSerializer::class) val alarmTime: LocalTime,
    val alarmReplayCnt: Int,
    val alarmReplayTime: Int,
    val isAlarmOn: Boolean,
    @Contextual val workTypeTitle: String?,
    @Contextual val workTypeColor: String?,
)

fun AlarmResponse.toDomainModel(): Alarm {
    return Alarm(
        alarmId = alarmId,
        alarmTime = alarmTime,
        alarmReplayCnt = alarmReplayCnt,
        alarmReplayTime = alarmReplayTime,
        isAlarmOn = isAlarmOn,
        workTypeTitle = workTypeTitle,
        workTypeColor = workTypeColor
    )
}