package com.org.egglog.data.retrofit

import kotlinx.serialization.Serializable

@Serializable
data class DataHeader(
    val successCode: Int,
    val resultCode: String,
    val resultMessage: String?
)

@Serializable
data class CommonResponse<T> (
    val dataHeader: DataHeader,
    val dataBody: T
)