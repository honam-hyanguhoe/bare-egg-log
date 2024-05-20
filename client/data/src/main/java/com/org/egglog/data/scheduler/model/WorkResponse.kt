package com.org.egglog.data.scheduler.model

import androidx.room.PrimaryKey
import com.org.egglog.domain.scheduler.model.Work
import kotlinx.serialization.Serializable

@Serializable
data class WorkResponse(
    @PrimaryKey val workId : Int,
    val workDate : String,
    val workType : WorkTypeResponse
)

fun WorkResponse.toDomainModel() : Work{
    return Work(
        workId, workDate, workType.toDomainModel()
    )
}