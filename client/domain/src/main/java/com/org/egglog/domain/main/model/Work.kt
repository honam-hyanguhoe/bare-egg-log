package com.org.egglog.domain.main.model

import kotlinx.serialization.Serializable

@Serializable
data class Work(
    val workId: Int,
    val workDate: String,
    val workType: WorkType
)