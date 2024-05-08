package com.org.egglog.data.community.model

import com.org.egglog.domain.community.model.GroupData
import kotlinx.serialization.Serializable

@Serializable
data class CommunityGroup (
    val groupId: Int,
    val groupImage: Int,
    val groupName: String,
    val admin: String
)

fun CommunityGroup.toDomainModel(): GroupData {
    return GroupData(
        groupId, groupImage, groupName, admin
    )
}