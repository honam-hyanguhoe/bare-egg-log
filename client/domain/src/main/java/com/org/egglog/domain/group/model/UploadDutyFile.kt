package com.org.egglog.domain.group.model

import kotlinx.serialization.Serializable

@Serializable
data class UploadDutyFile(
    val date: String,
    val dutyList: List<FormattedFile>,
    val customWorkTag: DutyTag,
    val userName: String,
    val day: String
)
