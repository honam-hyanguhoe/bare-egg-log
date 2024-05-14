package com.org.egglog.data.group.model

import com.org.egglog.domain.group.model.Admin
import com.org.egglog.domain.group.model.GroupMember
import com.org.egglog.domain.group.model.Member
import kotlinx.serialization.Serializable

@Serializable
data class GroupMemberDto(
    val userId : Long?,
    val groupId : Long,
    val userName : String,
    val profileImgUrl : String,
    val isAdmin : Boolean,
    val hospitalName : String
)

fun GroupMemberDto.toDomainModel() : GroupMember {
    return GroupMember(
        userId, groupId, userName, profileImgUrl, isAdmin, hospitalName
    )
}
