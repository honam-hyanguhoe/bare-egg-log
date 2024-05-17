package com.org.egglog.data.setting.usecase

import com.org.egglog.data.setting.model.toDomainModel
import com.org.egglog.data.setting.service.SettingService
import com.org.egglog.domain.setting.model.Notification
import com.org.egglog.domain.setting.usecase.GetNotificationListUseCase
import javax.inject.Inject

class GetNotificationListUseCaseImpl @Inject constructor(
    private val settingService: SettingService
): GetNotificationListUseCase {
    override suspend fun invoke(accessToken: String): Result<List<Notification>?> = kotlin.runCatching {
        settingService.getNotificationList(accessToken).dataBody?.map { it.toDomainModel() }
    }
}