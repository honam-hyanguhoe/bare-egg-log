package com.org.egglog.data.myCalendar.usecase

import com.org.egglog.data.myCalendar.model.PersonalScheduleParam
import com.org.egglog.data.myCalendar.service.MyCalendarService
import com.org.egglog.domain.myCalendar.usecase.CreatePersonalScheduleUseCase
import java.time.LocalDateTime
import javax.inject.Inject

class CreatePersonalScheduleUseCaseImpl @Inject constructor(
    private val myCalendarService: MyCalendarService
) : CreatePersonalScheduleUseCase {

    override suspend fun invoke(
        accessToken: String,
        eventTitle: String,
        eventContent: String,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        calendarGroupId: Long
    ): Result<Boolean> = kotlin.runCatching {

        val requestParam = PersonalScheduleParam(
            eventTitle,
            eventContent,
            startDate,
            endDate,
            calendarGroupId
        ).toRequestBody()

        val response = myCalendarService.createPersonalSchedule(accessToken, requestParam)

        if (response.dataHeader.successCode == 0) {
            true
        } else {
            throw Exception("개인 일정 등록 에러 발생 ${response.dataHeader.resultCode} : ${response.dataHeader.resultMessage}")
        }
    }
}