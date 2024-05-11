package com.org.egglog.data.setting.usecase

import com.org.egglog.data.setting.model.toDomainModel
import com.org.egglog.data.setting.service.SettingService
import com.org.egglog.domain.setting.model.CalendarGroup
import com.org.egglog.domain.setting.usecase.GetCalendarGroupListUseCase
import javax.inject.Inject

class GetCalendarGroupListUseCaseImpl @Inject constructor(
    private val settingService: SettingService
): GetCalendarGroupListUseCase {
    override suspend fun invoke(accessToken: String): Result<List<CalendarGroup>?> = kotlin.runCatching {
        settingService.getCalendarGroupList(accessToken).dataBody?.map { it.toDomainModel() }
    }
}