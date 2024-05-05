package com.org.egglog.domain.auth.model

import java.time.LocalDateTime

data class HospitalAuth(
    val empNo: String,
    val auth: Boolean,
    val authRequestTime: LocalDateTime?,
    val confirmTime: LocalDateTime?,
    val nurseCertificationImgUrl: String,
    val hospitalCertificationImgUrl: String,
    val hospitalInfo: UserHospital?
)