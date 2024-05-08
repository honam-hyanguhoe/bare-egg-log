package com.org.egglog.presentation.data

import com.org.egglog.domain.community.model.GroupData

data class GroupInfo (
    val groupId: Int,
    val groupImage: Int,
    val groupName: String,
    val admin: String
)

fun GroupData.toUiModel(): GroupInfo{
    return GroupInfo(groupId,groupImage, groupName, admin)
}