package com.org.egglog.domain.auth.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import util.LocalDateTimeSerializer
import java.time.LocalDateTime

@Serializable
data class HospitalAuth(
    val empNo: String,
    val auth: Boolean,
    @Serializable(with = LocalDateTimeSerializer::class) val authRequestTime: LocalDateTime?,
    @Serializable(with = LocalDateTimeSerializer::class) val confirmTime: LocalDateTime?,
    val nurseCertificationImgUrl: String?,
    val hospitalCertificationImgUrl: String?,
    @Contextual val hospitalInfo: UserHospital?
)