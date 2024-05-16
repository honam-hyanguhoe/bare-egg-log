package com.org.egglog.data.setting.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.org.egglog.data.util.LocalTimeSerializer
import com.org.egglog.domain.setting.model.Alarm
import com.org.egglog.domain.setting.model.AlarmUpdate
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalTime

@Entity
@Serializable
data class AlarmUpdateResponse(
    @PrimaryKey val alarmId: Long,
    @Serializable(with = LocalTimeSerializer::class) val alarmTime: LocalTime,
    val alarmReplayCnt: Int,
    val alarmReplayTime: Int,
    val isAlarmOn: Boolean,
    val workTypeTitle: String,
    @Contextual val workTypeColor: String?,
)

fun AlarmUpdateResponse.toDomainModel(): AlarmUpdate {
    return AlarmUpdate(
        alarmId = alarmId,
        alarmTime = alarmTime,
        alarmReplayCnt = alarmReplayCnt,
        alarmReplayTime = alarmReplayTime,
        isAlarmOn = isAlarmOn,
        workTypeTitle = workTypeTitle,
        workTypeColor = workTypeColor
    )
}