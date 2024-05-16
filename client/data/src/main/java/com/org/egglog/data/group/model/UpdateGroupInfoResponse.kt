package com.org.egglog.data.group.model

import com.org.egglog.domain.group.model.Admin
import com.org.egglog.domain.group.model.Group
import com.org.egglog.domain.group.model.GroupDuty
import com.org.egglog.domain.group.model.GroupInfo
import com.org.egglog.domain.group.model.GroupMember
import com.org.egglog.domain.group.model.Member
import kotlinx.serialization.Serializable

@Serializable
data class UpdateGroupInfoResponse(
    val groupId : Long,
    val groupName : String
)

fun UpdateGroupInfoResponse.toDomainModel(): GroupInfo {
    return GroupInfo(
        id = groupId,
        isAdmin = true,
        admin = null,
        groupImage = 1,
        groupName = groupName,
        groupMembers = emptyList()
    )
}


