package com.org.egglog.domain.setting.usecase

import com.org.egglog.domain.setting.model.AlarmStatusParam
import com.org.egglog.domain.setting.model.AlarmUpdate

interface UpdateAlarmStatusUseCase {
    suspend operator fun invoke(accessToken: String, alarmStatusParam: AlarmStatusParam): Result<AlarmUpdate?>
}