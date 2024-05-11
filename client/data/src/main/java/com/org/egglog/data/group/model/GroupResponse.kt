package com.org.egglog.data.group.model

import com.org.egglog.domain.group.model.Group
import kotlinx.serialization.Serializable

@Serializable
data class GroupResponse(
    val groupId: Long,
    val admin: String,
    val groupName: String,
    val groupImage: Int,
    val memberCount: Int,
)

fun GroupResponse.toDomainModel() : Group{
    return Group(
        groupId, admin, groupName, groupImage, memberCount
    )
}

