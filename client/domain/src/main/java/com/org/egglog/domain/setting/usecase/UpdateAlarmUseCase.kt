package com.org.egglog.domain.setting.usecase

import com.org.egglog.domain.setting.model.AlarmParam
import com.org.egglog.domain.setting.model.AlarmUpdate

interface UpdateAlarmUseCase {
    suspend operator fun invoke(accessToken: String, alarmParam: AlarmParam): Result<AlarmUpdate?>
}