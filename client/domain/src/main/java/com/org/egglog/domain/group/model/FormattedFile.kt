package com.org.egglog.domain.group.model

import kotlinx.serialization.Serializable

@Serializable
data class FormattedFile(
    val employeeId: String,
    val name: String,
    val work: Map<String, String>
)
