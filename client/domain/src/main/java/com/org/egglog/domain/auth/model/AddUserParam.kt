package com.org.egglog.domain.auth.model

data class AddUserParam(
    val userName: String,
    val hospitalId: Int,
    val empNo: String,
    val fcmToken: String
)