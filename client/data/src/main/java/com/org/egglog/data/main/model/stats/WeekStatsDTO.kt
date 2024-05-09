package com.org.egglog.data.main.model.stats

import com.org.egglog.domain.main.model.WeekStats
import kotlinx.serialization.Serializable

@Serializable
data class WeekStatsDTO(
    val week : Int,
    val data : WeekDataDTO
)

fun WeekStatsDTO.toDomainModel(): WeekStats {
    return WeekStats(
        week = week,
        data = data.toDomainModel()
    )
}
