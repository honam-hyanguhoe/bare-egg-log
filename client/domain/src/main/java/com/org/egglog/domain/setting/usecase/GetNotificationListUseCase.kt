package com.org.egglog.domain.setting.usecase

import com.org.egglog.domain.setting.model.Notification

interface GetNotificationListUseCase {
    suspend operator fun invoke(accessToken: String): Result<List<Notification>?>
}