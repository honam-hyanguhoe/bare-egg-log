package com.org.egglog.data.group.model

import com.org.egglog.domain.group.model.Admin
import com.org.egglog.domain.group.model.Group
import com.org.egglog.domain.group.model.GroupInfo
import kotlinx.serialization.Serializable

@Serializable
data class GroupInfoResponse(
    val id: Long,  // groupId
    val isAdmin: Boolean,
    val admin: AdminDto?,
    val groupImage: Int,
    val groupName: String,
    val groupMembers: List<GroupMemberDto>?
)

fun GroupInfoResponse.toDomainModel(): GroupInfo {
    return GroupInfo(
        id,
        isAdmin,
        admin?.toDomainModel(),
        groupImage,
        groupName,
        groupMembers?.mapNotNull { it.toDomainModel() }
    )
}

