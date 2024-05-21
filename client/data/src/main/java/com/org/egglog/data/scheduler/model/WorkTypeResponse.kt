package com.org.egglog.data.scheduler.model

import com.org.egglog.domain.scheduler.model.WorkType
import kotlinx.serialization.Serializable

@Serializable
data class WorkTypeResponse (
    val workTypeId: Int,
    val title: String,
    val color: String,
    val workTypeImgUrl: String,
    val workTag: String,
    val startTime: String,
    val workTime: String
)

fun WorkTypeResponse.toDomainModel(): WorkType {
    return WorkType(workTypeId, title, color, workTypeImgUrl, workTag, startTime, workTime)
}