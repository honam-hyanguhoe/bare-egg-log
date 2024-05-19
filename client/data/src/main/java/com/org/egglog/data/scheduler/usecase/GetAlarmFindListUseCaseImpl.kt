package com.org.egglog.data.scheduler.usecase

import com.org.egglog.data.scheduler.model.toDomainModel
import com.org.egglog.data.scheduler.service.AlarmService
import com.org.egglog.domain.scheduler.model.AlarmFind
import com.org.egglog.domain.scheduler.usecase.GetAlarmFindListUseCase
import javax.inject.Inject

class GetAlarmFindListUseCaseImpl @Inject constructor(
    private val alarmService: AlarmService
): GetAlarmFindListUseCase {
    override suspend fun invoke(accessToken: String, startDate: String, endDate: String): Result<AlarmFind?> = kotlin.runCatching {
        alarmService.getAlarmFindList(accessToken = accessToken, startDate = startDate, endDate = endDate).dataBody?.toDomainModel()
    }
}