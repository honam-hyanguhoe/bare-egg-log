package com.org.egglog.domain.auth.model

data class Refresh constructor(
    val refreshToken: String,
    val accessToken: String
)