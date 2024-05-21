package com.org.egglog.domain.setting.model

import java.time.LocalDateTime

data class Notification (
    val notificationId: Long,
    val type: NotificationType,
    val status: Boolean,
    val updatedAt: LocalDateTime
)