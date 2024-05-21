package com.org.egglog.data.auth.model

import androidx.room.Entity
import com.org.egglog.domain.auth.model.Token
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class TokenResponse(
    val accessToken: String,
    val refreshToken: String
)

fun TokenResponse.toDomainModel(): Token {
    return Token(
        refreshToken = this.refreshToken,
        accessToken = this.accessToken
    )
}