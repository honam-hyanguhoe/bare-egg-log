package com.org.egglog.domain.group.model

import kotlinx.serialization.Serializable

@Serializable
data class Member(
    val userId : Long,
    val userName : String,
    val profileImgUrl : String
)
