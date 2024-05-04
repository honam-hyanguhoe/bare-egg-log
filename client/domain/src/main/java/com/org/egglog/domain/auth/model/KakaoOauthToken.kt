package com.org.egglog.domain.auth.model

import java.util.Date

data class KakaoOauthToken(
    val accessToken: String,
    val accessTokenExpiresAt: Date,
    val refreshToken: String,
    val refreshTokenExpiresAt: Date,
    val idToken: String? = null,
    val scopes: List<String>? = null
)