package com.org.egglog.data.auth.model

import androidx.room.Entity
import com.org.egglog.data.util.LocalDateTimeSerializer
import com.org.egglog.domain.auth.model.HospitalAuth
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Entity
@Serializable
data class HospitalAuthResponse(
    val empNo: String,
    val auth: Boolean,
    @Serializable(with = LocalDateTimeSerializer::class) val authRequestTime: LocalDateTime?,
    @Serializable(with = LocalDateTimeSerializer::class) val confirmTime: LocalDateTime?,
    val nurseCertificationImgUrl: String,
    val hospitalCertificationImgUrl: String,
    @Contextual val hospitalInfo: UserHospitalResponse?
)

fun HospitalAuthResponse.toDomainModel(): HospitalAuth {
        return HospitalAuth(
            empNo, auth, authRequestTime, confirmTime, nurseCertificationImgUrl, hospitalCertificationImgUrl, hospitalInfo?.toDomainModel()
        )
}