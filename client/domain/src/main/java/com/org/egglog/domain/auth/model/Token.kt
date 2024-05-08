package com.org.egglog.domain.auth.model

data class Token (
    val refreshToken: String,
    val accessToken: String
)