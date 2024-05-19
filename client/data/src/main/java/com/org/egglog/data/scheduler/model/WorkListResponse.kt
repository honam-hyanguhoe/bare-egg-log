package com.org.egglog.data.scheduler.model

import com.org.egglog.domain.scheduler.model.WorkList
import kotlinx.serialization.Serializable

@Serializable
data class WorkListResponse(
    val work: WorkResponse,
    val alarm: AlarmResponse
)

fun WorkListResponse.toDomainModel(): WorkList {
    return WorkList(
        work = work.toDomainModel(),
        alarm = alarm.toDomainModel()
    )
}