package com.org.egglog.data.setting.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.org.egglog.data.util.LocalDateTimeSerializer
import com.org.egglog.domain.setting.model.Notification
import com.org.egglog.domain.setting.model.NotificationType
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Entity
@Serializable
data class NotificationResponse(
    @PrimaryKey val notificationId: Long,
    val type: NotificationType,
    val status: Boolean,
    @Serializable(with = LocalDateTimeSerializer::class) val updatedAt: LocalDateTime
)

fun NotificationResponse.toDomainModel(): Notification {
    return Notification(
        notificationId, type, status, updatedAt
    )
}