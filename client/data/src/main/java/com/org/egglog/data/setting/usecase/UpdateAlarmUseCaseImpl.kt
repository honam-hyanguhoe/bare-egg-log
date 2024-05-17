package com.org.egglog.data.setting.usecase

import com.org.egglog.data.setting.model.AlarmRequest
import com.org.egglog.data.setting.model.toDomainModel
import com.org.egglog.data.setting.service.SettingService
import com.org.egglog.domain.setting.model.AlarmParam
import com.org.egglog.domain.setting.model.AlarmUpdate
import com.org.egglog.domain.setting.usecase.UpdateAlarmUseCase
import javax.inject.Inject

class UpdateAlarmUseCaseImpl @Inject constructor(
    private val settingService: SettingService
): UpdateAlarmUseCase {
    override suspend fun invoke(accessToken: String, alarmParam: AlarmParam): Result<AlarmUpdate?> = kotlin.runCatching {
        val requestBody = AlarmRequest(
            alarmId = alarmParam.alarmId,
            alarmTime = alarmParam.alarmTime,
            workTypeId = alarmParam.workTypeId,
            replayCnt = alarmParam.replayCnt,
            replayTime = alarmParam.replayTime
        ).toRequestBody()
        settingService.updateAlarm(accessToken = accessToken, requestBody = requestBody).dataBody?.toDomainModel()
    }
}