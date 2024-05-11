package com.org.egglog.data.setting.usecase

import com.org.egglog.data.setting.model.AddCalendarGroupRequest
import com.org.egglog.data.setting.model.toDomainModel
import com.org.egglog.data.setting.service.SettingService
import com.org.egglog.domain.setting.model.AddCalendarGroupParam
import com.org.egglog.domain.setting.model.CalendarGroup
import com.org.egglog.domain.setting.usecase.PostCalendarGroupUseCase
import javax.inject.Inject

class PostCalendarGroupUseCaseImpl @Inject constructor(
    private val settingService: SettingService
): PostCalendarGroupUseCase {
    override suspend fun invoke(accessToken: String, addCalendarGroupParam: AddCalendarGroupParam): Result<CalendarGroup?> = kotlin.runCatching {
        val requestBody = AddCalendarGroupRequest(
            alias = addCalendarGroupParam.alias,
            url = addCalendarGroupParam.url,
            inUrl = addCalendarGroupParam.inUrl
        ).toRequestBody()
        settingService.postCalendarGroup(accessToken, requestBody).dataBody?.toDomainModel()
    }
}