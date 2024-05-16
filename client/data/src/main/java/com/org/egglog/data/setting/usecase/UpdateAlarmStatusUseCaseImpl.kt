package com.org.egglog.data.setting.usecase

import com.org.egglog.data.setting.model.AlarmStatusRequest
import com.org.egglog.data.setting.model.toDomainModel
import com.org.egglog.data.setting.service.SettingService
import com.org.egglog.domain.setting.model.AlarmStatusParam
import com.org.egglog.domain.setting.model.AlarmUpdate
import com.org.egglog.domain.setting.usecase.UpdateAlarmStatusUseCase
import javax.inject.Inject

class UpdateAlarmStatusUseCaseImpl @Inject constructor(
    private val settingService: SettingService
): UpdateAlarmStatusUseCase {
    override suspend fun invoke(accessToken: String, alarmStatusParam: AlarmStatusParam): Result<AlarmUpdate?> = kotlin.runCatching {
        val requestBody = AlarmStatusRequest(
            alarmId = alarmStatusParam.alarmId,
            isAlarmOn = alarmStatusParam.isAlarmOn
        ).toRequestBody()
        settingService.updateAlarmStatus(accessToken = accessToken, requestBody = requestBody).dataBody?.toDomainModel()
    }
}