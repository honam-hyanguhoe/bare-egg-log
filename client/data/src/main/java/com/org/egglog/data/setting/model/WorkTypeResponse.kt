package com.org.egglog.data.setting.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.org.egglog.domain.setting.model.WorkType
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class WorkTypeResponse(
    @PrimaryKey val workTypeId: Long,
    val title: String,
    val color: String,
    val workTypeImgUrl: String,
    val workTag: String,
    val startTime: String,
    val workTime: String,
)

fun WorkTypeResponse.toDomainModel(): WorkType {
    return WorkType(
        workTypeId = workTypeId,
        title = title,
        color = color,
        workTypeImgUrl = workTypeImgUrl,
        workTag = workTag,
        startTime = startTime,
        workTime = workTime
    )
}