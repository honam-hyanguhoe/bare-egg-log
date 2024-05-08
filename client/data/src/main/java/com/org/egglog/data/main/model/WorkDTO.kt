package com.org.egglog.data.main.model

import androidx.room.PrimaryKey
import com.org.egglog.domain.main.model.WeeklyWork
import com.org.egglog.domain.main.model.Work
import kotlinx.serialization.Serializable

@Serializable
data class WorkDTO(
    @PrimaryKey val workId : Int,
    val workDate : String,
    val workType : WorkTypeDTO
)

fun WorkDTO.toDomainModel() : Work{
    return Work(
        workId, workDate, workType.toDomainModel()
    )
}