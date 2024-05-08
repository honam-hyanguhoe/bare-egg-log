package com.org.egglog.domain.auth.model

data class UserModifyParam(
    val userName: String,
    val hospitalId: Long,
    val empNo: String
)