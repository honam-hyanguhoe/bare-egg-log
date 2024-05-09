package com.org.egglog.data.main.model

import com.org.egglog.domain.main.model.WeeklyWork
import com.org.egglog.domain.main.model.WorkStats
import kotlinx.serialization.Serializable

@Serializable
data class WorkStatsResponse(
    val month : String,
//    val weeks: List<WeekStatsDTO>
)

