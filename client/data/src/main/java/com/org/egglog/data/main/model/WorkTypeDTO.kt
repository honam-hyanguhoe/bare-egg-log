package com.org.egglog.data.main.model

import com.org.egglog.domain.main.model.WeeklyWork
import com.org.egglog.domain.main.model.WorkType
import kotlinx.serialization.Serializable

@Serializable
data class WorkTypeDTO(
    val workTypeId : Int,
    val title : String,
    val color : String,
    val workTypeImgUrl : String,
    val workTag : String,
    val startTime : String,
    val workTime : String,
)

fun WorkTypeDTO.toDomainModel() : WorkType {
    return WorkType(
        workTypeId, title, color, workTypeImgUrl, workTag, startTime, workTime
    )
}