package com.org.egglog.data.myCalendar.usecase

import com.org.egglog.data.myCalendar.model.toDomainModel
import com.org.egglog.data.myCalendar.service.MyCalendarService
import com.org.egglog.domain.myCalendar.model.EventListData
import com.org.egglog.domain.myCalendar.usecase.GetDetailPersonalScheduleUseCase
import javax.inject.Inject

class GetDetailPersonalScheduleUseCaseImpl @Inject constructor(
    private val myCalendarService: MyCalendarService
): GetDetailPersonalScheduleUseCase {
    override suspend fun invoke(accessToken: String, eventId: Int): Result<EventListData> = kotlin.runCatching {
        myCalendarService.getDetailPersonalSchedule(accessToken,eventId).dataBody!!.toDomainModel()
    }
}