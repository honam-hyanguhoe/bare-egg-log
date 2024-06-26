package com.org.egglog.domain.group.model

import kotlinx.serialization.Serializable

@Serializable
data class Admin(
    val userId: Long?,
    val groupId: Long,
    val userName: String,
    val profileImgUrl: String,
    val isAdmin: Boolean,
)

