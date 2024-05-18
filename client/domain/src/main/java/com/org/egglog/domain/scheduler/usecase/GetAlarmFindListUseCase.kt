package com.org.egglog.domain.scheduler.usecase

import com.org.egglog.domain.scheduler.model.AlarmFind

interface GetAlarmFindListUseCase {

    suspend operator fun invoke(accessToken: String, startDate: String, endDate: String): Result<AlarmFind?>
}