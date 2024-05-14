package com.org.egglog.domain.group.model

import kotlinx.serialization.Serializable

@Serializable
data class Group(
    val groupId: Long,
    val admin: String,
    val groupName: String,
    val groupImage: Int,
    val memberCount: Int,
)