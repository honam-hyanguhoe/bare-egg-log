package com.org.egglog.data.scheduler.usecase

import com.org.egglog.data.scheduler.model.toDomainModel
import com.org.egglog.data.scheduler.service.AlarmService
import com.org.egglog.domain.scheduler.model.PersonalScheduleData
import com.org.egglog.domain.scheduler.usecase.GetAlarmPersonalScheduleUseCase
import javax.inject.Inject

class GetAlarmPersonalScheduleUseCaseImpl @Inject constructor(
    private val alarmService: AlarmService
): GetAlarmPersonalScheduleUseCase {
    override suspend fun invoke(
        accessToken: String,
        startDate: String,
        endDate: String,
        userId: Long,
        calendarGroupId: Long
    ): Result<List<PersonalScheduleData>?> = kotlin.runCatching {
        alarmService.getPersonalList(accessToken,startDate,endDate,userId,calendarGroupId).dataBody?.map{it.toDomainModel()}
    }
}