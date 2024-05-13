package com.org.egglog.data.group.model

import com.org.egglog.data.main.model.WeeklyWorkReponse
import com.org.egglog.data.main.model.WorkDTO
import com.org.egglog.data.main.model.toDomainModel
import com.org.egglog.domain.group.model.GroupMembersWork
import com.org.egglog.domain.main.model.WeeklyWork

data class MembersWorkResponse(
    val workList: List<WorkDTO>,
)

fun MembersWorkResponse.toDomainModel(): GroupMembersWork {
    return GroupMembersWork(
        workList = workList.mapNotNull { it.toDomainModel() },
    )
}