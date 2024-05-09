package com.org.egglog.data.main.model.stats

import com.org.egglog.domain.main.model.WeekData
import kotlinx.serialization.Serializable

@Serializable
data class WeekDataDTO(
    val DAY: Int,
    val EVE: Int,
    val NIGHT: Int,
    val OFF: Int
)

fun WeekDataDTO.toDomainModel(): WeekData {
    return WeekData(
        DAY, EVE, NIGHT, OFF
    )
}

