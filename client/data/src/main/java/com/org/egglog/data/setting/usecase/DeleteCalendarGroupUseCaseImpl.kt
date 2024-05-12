package com.org.egglog.data.setting.usecase

import com.org.egglog.data.setting.service.SettingService
import com.org.egglog.domain.setting.usecase.DeleteCalendarGroupUseCase
import javax.inject.Inject

class DeleteCalendarGroupUseCaseImpl @Inject constructor(
    private val settingService: SettingService
): DeleteCalendarGroupUseCase {
    override suspend fun invoke(calendarGroupId: Long, accessToken: String): Result<Unit?> = kotlin.runCatching {
        settingService.deleteCalendarGroup(calendarGroupId = calendarGroupId, accessToken = accessToken).dataBody
    }
}