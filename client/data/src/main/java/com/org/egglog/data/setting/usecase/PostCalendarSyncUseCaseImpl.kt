package com.org.egglog.data.setting.usecase

import com.org.egglog.data.setting.service.SettingService
import com.org.egglog.domain.setting.usecase.PostCalendarSyncUseCase
import javax.inject.Inject

class PostCalendarSyncUseCaseImpl @Inject constructor(
    private val settingService: SettingService
): PostCalendarSyncUseCase {
    override suspend fun invoke(accessToken: String): Result<Unit?> = kotlin.runCatching {
        settingService.postCalendarSync(accessToken).dataBody
    }
}