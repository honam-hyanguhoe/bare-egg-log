package com.org.egglog.domain.main.model

import kotlinx.serialization.Serializable

@Serializable
data class WorkStats(
    val month: String,
    val weeks: List<WeekStats>
)

