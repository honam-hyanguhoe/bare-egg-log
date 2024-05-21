package com.org.egglog.data.myCalendar.usecase

import com.org.egglog.data.myCalendar.model.toDomainModel
import com.org.egglog.data.myCalendar.service.MyCalendarService
import com.org.egglog.domain.myCalendar.model.PersonalScheduleData
import com.org.egglog.domain.myCalendar.usecase.GetPersonalScheduleUseCase
import javax.inject.Inject

class GetPersonalScheduleUseCaseImpl @Inject constructor(
    private val myCalendarService: MyCalendarService
): GetPersonalScheduleUseCase {
    override suspend fun invoke(
        accessToken: String,
        startDate: String,
        endDate: String,
        userId: Long,
        calendarGroupId: Long
    ): Result<List<PersonalScheduleData>> = kotlin.runCatching {
        myCalendarService.getPersonalList(accessToken,startDate,endDate,userId,calendarGroupId).dataBody!!.map{it.toDomainModel()}
    }
}