package com.org.egglog.domain.auth.model

data class UserModifyParam(
    val userName: String,
    val profileImgUrl: String,
    val selectHospitalId: Long,
    val empNo: String
)