package com.org.egglog.data.setting.usecase

import com.org.egglog.data.setting.model.NotificationListRequest
import com.org.egglog.data.setting.model.NotificationRequest
import com.org.egglog.data.setting.model.toDomainModel
import com.org.egglog.data.setting.service.SettingService
import com.org.egglog.domain.setting.model.Notification
import com.org.egglog.domain.setting.model.NotificationListParam
import com.org.egglog.domain.setting.usecase.UpdateNotificationUseCase
import javax.inject.Inject

class UpdateNotificationUseCaseImpl @Inject constructor(
    private val settingService: SettingService
): UpdateNotificationUseCase {
    override suspend fun invoke(accessToken: String, notificationListParam: NotificationListParam): Result<List<Notification>?> = kotlin.runCatching {
        val requestBody = NotificationListRequest(
            notificationSetList = notificationListParam.notificationSetList.map {
                NotificationRequest(it.notificationId, it.status)
            }
        ).toRequestBody()
        settingService.updateNotification(accessToken, requestBody).dataBody?.map { it.toDomainModel() }
    }
}