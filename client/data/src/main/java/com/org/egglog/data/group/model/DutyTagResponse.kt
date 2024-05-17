package com.org.egglog.data.group.model

import com.org.egglog.domain.group.model.DutyTag
import com.org.egglog.domain.group.model.GroupInfo
import kotlinx.serialization.Serializable

@Serializable
data class DutyTagResponse(
    val day: String,
    val eve: String,
    val night: String,
    val off: String
)

fun DutyTagResponse.toDomainModel(): DutyTag {
    return DutyTag(
        day, eve, night, off
    )
}

