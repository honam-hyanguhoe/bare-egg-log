package com.org.egglog.data.auth.model

import androidx.room.Entity
import com.org.egglog.domain.auth.model.Refresh
import com.org.egglog.domain.auth.model.Token
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class RefreshResponse(
    @Contextual val tokens: TokenResponse,
    @Contextual val userInfo: UserResponse?
)

fun RefreshResponse.toDomainModel(): Refresh {
    return Refresh(
        tokens.toDomainModel(),
        userInfo?.toDomainModel()
    )
}