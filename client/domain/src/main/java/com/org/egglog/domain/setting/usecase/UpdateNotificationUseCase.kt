package com.org.egglog.domain.setting.usecase

import com.org.egglog.domain.setting.model.Notification
import com.org.egglog.domain.setting.model.NotificationListParam

interface UpdateNotificationUseCase {
    suspend operator fun invoke(accessToken: String, notificationListParam: NotificationListParam): Result<List<Notification>?>
}