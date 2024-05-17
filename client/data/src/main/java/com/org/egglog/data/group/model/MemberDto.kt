package com.org.egglog.data.group.model

import com.org.egglog.domain.group.model.Admin
import com.org.egglog.domain.group.model.Member
import kotlinx.serialization.Serializable

@Serializable
data class MemberDto(
    val userId : Long,
    val userName : String,
    val profileImgUrl : String
)

fun MemberDto.toDomainModel() : Member {
    return Member(
       userId, userName, profileImgUrl
    )
}
