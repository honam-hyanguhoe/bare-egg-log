package com.org.egglog.domain.group.model

import kotlinx.serialization.Serializable

@Serializable
data class DutyTag(
    val day: String,
    val eve: String,
    val night: String,
    val off: String
)
