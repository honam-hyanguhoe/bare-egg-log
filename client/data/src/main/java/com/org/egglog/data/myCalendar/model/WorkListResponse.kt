package com.org.egglog.data.myCalendar.model

import com.org.egglog.domain.myCalendar.model.WorkListData
import kotlinx.serialization.Serializable

@Serializable
data class WorkListResponse (
    val workId: Long,
    val workDate: String,
    val workType: WorkTypeResponse
)

fun WorkListResponse.toDomainModel(): WorkListData {
    return WorkListData(
        workId, workDate, workType.toDomainModel()
    )

}