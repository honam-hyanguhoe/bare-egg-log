package com.org.egglog.domain.main.model

import kotlinx.serialization.Serializable

@Serializable
data class WeekStats(
    val week: Int,
    val data: WeekData
)



