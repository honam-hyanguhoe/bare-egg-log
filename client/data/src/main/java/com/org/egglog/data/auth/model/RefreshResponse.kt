package com.org.egglog.data.auth.model

import androidx.room.Entity
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class RefreshResponse(
    val refreshToken: String,
    val accessToken: String
)