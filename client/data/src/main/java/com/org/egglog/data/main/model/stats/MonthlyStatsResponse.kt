package com.org.egglog.data.main.model.stats

import com.org.egglog.domain.main.model.WorkStats
import kotlinx.serialization.Serializable

@Serializable
data class MonthlyStatsResponse(
    val month: String,
    val weeks: List<WeekStatsDTO>
)

fun MonthlyStatsResponse.toDomainModel() : WorkStats {
    return WorkStats(
        month, weeks.mapNotNull { it.toDomainModel() }
    )
}