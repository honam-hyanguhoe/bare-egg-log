package com.org.egglog.domain.scheduler.usecase

import com.org.egglog.domain.scheduler.model.PersonalScheduleData


interface GetAlarmPersonalScheduleUseCase {

    suspend operator fun invoke(accessToken:String, startDate: String, endDate: String, userId: Long, calendarGroupId: Long): Result<List<PersonalScheduleData>?>
}