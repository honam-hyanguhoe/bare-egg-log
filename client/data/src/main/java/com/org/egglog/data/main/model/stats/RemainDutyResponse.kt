package com.org.egglog.data.main.model.stats

import com.org.egglog.domain.main.model.RemainDuty
import kotlinx.serialization.Serializable

@Serializable
data class RemainDutyResponse(
    val name : String,
    val value: Int,
    val color : String
)

fun RemainDutyResponse.toDomainModel() : RemainDuty {
    return RemainDuty(
        name, value, color
    )
}
