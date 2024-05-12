package com.org.egglog.domain.group.model

import kotlinx.serialization.Serializable

@Serializable
data class GroupInfo(
    val id: Long,  // groupId
    val isAdmin: Boolean,
    val admin: Admin?,
    val groupImage: Int,
    val groupName: String,
    val groupMembers : List<String>?
)
