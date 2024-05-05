package com.org.egglog.domain.auth.model

import java.time.LocalDateTime

data class UserDetail(
    val id: Long,
    val email: String,
    val userName: String,
    val selectedHospital: UserHospital?,
    val hospitalAuth: HospitalAuth?,
    val profileImgUrl: String,
    val userRole: String,
    val userStatus: String,
    val deviceToken: String?,
    val empNo: String?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
    val loginAt: LocalDateTime?
)
