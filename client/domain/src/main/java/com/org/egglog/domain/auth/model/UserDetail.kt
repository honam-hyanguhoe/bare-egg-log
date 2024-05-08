package com.org.egglog.domain.auth.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import util.LocalDateTimeSerializer
import java.time.LocalDateTime

@Serializable
data class UserDetail(
    val id: Long,
    val email: String,
    val userName: String,
    @Contextual val selectedHospital: UserHospital?,
    @Contextual val hospitalAuth: HospitalAuth?,
    val profileImgUrl: String,
    val userRole: String,
    val userStatus: String,
    val deviceToken: String?,
    val empNo: String?,
    @Serializable(with = LocalDateTimeSerializer::class) val createdAt: LocalDateTime?,
    @Serializable(with = LocalDateTimeSerializer::class) val updatedAt: LocalDateTime?,
    @Serializable(with = LocalDateTimeSerializer::class) val loginAt: LocalDateTime?,
    val workGroupId: Long
)
