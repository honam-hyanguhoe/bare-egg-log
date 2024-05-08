package com.org.egglog.data.main.model

import com.org.egglog.domain.main.model.WeekData
import com.org.egglog.domain.main.model.WorkStats
import kotlinx.serialization.Serializable

@Serializable
data class WeekStatsDTO(
    val week : Int,
    val data : WeekDataDTO
)

@Serializable
data class WeekDataDTO(
    val DAY : Int,
    val EVE : Int,
    val NIGHT : Int,
    val OFF : Int,
    val EDU : Int,
)


fun WeekStatsDTO.toDomainModel(): WorkStats {
    return WorkStats(
        month = month,
        weeks = List<WeekStatsDTO>
    )
}