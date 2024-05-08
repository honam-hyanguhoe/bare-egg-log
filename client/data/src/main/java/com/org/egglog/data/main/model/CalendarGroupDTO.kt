package com.org.egglog.data.main.model

import androidx.room.PrimaryKey
import com.org.egglog.domain.main.model.CalendarGroup
import com.org.egglog.domain.main.model.WeeklyWork
import kotlinx.serialization.Serializable

@Serializable
data class CalendarGroupDTO(
    @PrimaryKey val calendarGroupId : Int,
    val url : String?,
    val alias : String
)

fun CalendarGroupDTO.toDomainModel() : CalendarGroup {
    return CalendarGroup(
        calendarGroupId = calendarGroupId,
        url = url ?: "",
        alias = alias
    )
}