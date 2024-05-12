package com.org.egglog.data.group.model

import com.org.egglog.domain.group.model.Admin
import com.org.egglog.domain.group.model.Group
import kotlinx.serialization.Serializable

@Serializable
data class AdminDto(
    val userId: String?,
    val groupId: Long,
    val userName: String,
    val profileImgUrl: String,
    val isAdmin: Boolean,
)

fun AdminDto.toDomainModel() : Admin {
    return Admin(
        userId, groupId, userName, profileImgUrl, isAdmin
    )
}
