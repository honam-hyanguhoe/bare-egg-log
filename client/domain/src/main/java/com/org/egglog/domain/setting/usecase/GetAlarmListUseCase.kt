package com.org.egglog.domain.setting.usecase

import com.org.egglog.domain.setting.model.Alarm

interface GetAlarmListUseCase {
    suspend operator fun invoke(accessToken: String): Result<List<Alarm>?>
}