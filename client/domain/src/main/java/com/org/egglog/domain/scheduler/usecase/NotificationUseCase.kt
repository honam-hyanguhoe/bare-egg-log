package com.org.egglog.domain.scheduler.usecase

import java.time.LocalDateTime

interface NotificationUseCase {
    fun setNotification(
        key: Int,
        targetDateTime: LocalDateTime
    )

    fun cancelAlarm(key: Int)
}
