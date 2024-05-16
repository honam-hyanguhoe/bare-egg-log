package com.org.egglog.data.myCalendar.usecase

import com.org.egglog.data.myCalendar.model.PersonalScheduleParam
import com.org.egglog.data.myCalendar.service.MyCalendarService
import com.org.egglog.domain.myCalendar.usecase.ModifyPersonalScheduleUseCase
import java.time.LocalDateTime
import javax.inject.Inject

class ModifyPersonalScheduleUseCaseImpl @Inject constructor(
    private val myCalendarService: MyCalendarService
): ModifyPersonalScheduleUseCase {
    override suspend fun invoke(
        accessToken: String,
        eventId: Int,
        eventTitle: String,
        eventContent: String,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        calendarGroupId: Long
    ): Result<Boolean> = kotlin.runCatching {
        val requestBody = PersonalScheduleParam(eventTitle, eventContent, startDate, endDate, calendarGroupId).toRequestBody()

        val response = myCalendarService.modifyPersonalSchedule(accessToken, eventId, requestBody)

        if(response.dataHeader.successCode == 0) {
            true
        } else {
            throw Exception("일정 수정 오류 발생!! ${response.dataHeader.resultCode}: ${response.dataHeader.resultMessage}")
        }
    }
}