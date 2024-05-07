package com.org.egglog.domain.auth.model

import kotlinx.serialization.Serializable

@Serializable
data class UserHospital(
    val hospitalId: Long,
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
) {
    override fun toString(): String {
        return hospitalName
    }
}