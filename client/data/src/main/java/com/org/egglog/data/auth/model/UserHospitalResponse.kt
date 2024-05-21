package com.org.egglog.data.auth.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.org.egglog.domain.auth.model.UserHospital
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class UserHospitalResponse(
    @PrimaryKey val hospitalId: Long,
    val sidoCode: String?,
    val sido: String?,
    val gunguCode: String?,
    val gungu: String?,
    val dong: String?,
    val zipCode: String?,
    val address: String,
    val hospitalName: String,
    val lat: String?,
    val lng: String?
)

fun UserHospitalResponse.toDomainModel(): UserHospital {
    return UserHospital(
        hospitalId, sidoCode, sido, gunguCode, gungu, dong, zipCode, address, hospitalName, lat, lng
    )
}