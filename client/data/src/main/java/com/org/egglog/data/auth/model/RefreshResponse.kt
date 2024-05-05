package com.org.egglog.data.auth.model

import androidx.room.Entity
import com.org.egglog.domain.auth.model.Refresh
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class RefreshResponse(
    val accessToken: String,
    val refreshToken: String
)

fun RefreshResponse.toDomainModel(): Refresh {
    return Refresh(
        refreshToken = this.refreshToken,
        accessToken = this.accessToken
    )
}