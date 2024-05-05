package com.org.egglog.data.auth.model

import androidx.room.Entity
import com.org.egglog.data.util.LocalDateTimeSerializer
import com.org.egglog.domain.auth.model.UserDetail
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Entity
@Serializable
data class UserResponse(
    val id: Long,
    val email: String,
    val userName: String,
    @Contextual val selectedHospital: UserHospitalResponse?,
    @Contextual val hospitalAuth: HospitalAuthResponse?,
    val profileImgUrl: String,
    val userRole: UserRole,
    val userStatus: UserStatus,
    val deviceToken: String?,
    val empNo: String?,
    @Serializable(with = LocalDateTimeSerializer::class) val createdAt: LocalDateTime?,
    @Serializable(with = LocalDateTimeSerializer::class) val updatedAt: LocalDateTime?,
    @Serializable(with = LocalDateTimeSerializer::class) val loginAt: LocalDateTime?
)

fun UserResponse.toDomainModel(): UserDetail {
    return UserDetail(
        id = id,
        email = email,
        userName = userName,
        selectedHospital = selectedHospital?.toDomainModel(),
        hospitalAuth = hospitalAuth?.toDomainModel(),
        profileImgUrl = profileImgUrl,
        userRole = userRole.toString(),
        userStatus = userStatus.toString(),
        deviceToken = deviceToken,
        empNo = empNo,
        createdAt = createdAt,
        updatedAt = updatedAt,
        loginAt = loginAt
    )
}