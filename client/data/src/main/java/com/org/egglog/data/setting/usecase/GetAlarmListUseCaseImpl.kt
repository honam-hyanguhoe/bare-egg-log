package com.org.egglog.data.setting.usecase

import com.org.egglog.data.setting.model.toDomainModel
import com.org.egglog.data.setting.service.SettingService
import com.org.egglog.domain.setting.model.Alarm
import com.org.egglog.domain.setting.usecase.GetAlarmListUseCase
import javax.inject.Inject

class GetAlarmListUseCaseImpl @Inject constructor(
    private val settingService: SettingService
): GetAlarmListUseCase {
    override suspend fun invoke(accessToken: String): Result<List<Alarm>?> = kotlin.runCatching {
        settingService.getAlarmList(accessToken).dataBody?.map { it.toDomainModel() }
    }
}