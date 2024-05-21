package com.org.egglog.data.scheduler.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.org.egglog.domain.scheduler.model.CalendarGroup
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class CalendarGroupResponse(
    @PrimaryKey val calendarGroupId: Long,
    val url: String?,
    val alias: String
)

fun CalendarGroupResponse.toDomainModel(): CalendarGroup {
    return CalendarGroup(
        calendarGroupId = calendarGroupId,
        url = url,
        alias = alias
    )
}