package com.org.egglog.data.myCalendar.usecase

import com.org.egglog.data.myCalendar.service.MyCalendarService
import com.org.egglog.domain.myCalendar.usecase.DeletePersonalScheduleUseCase
import javax.inject.Inject

class DeletePersonalScheduleUseCaseImpl @Inject constructor(
    private val myCalendarService: MyCalendarService
): DeletePersonalScheduleUseCase {
    override suspend fun invoke(accessToken: String, eventId: Int): Result<Boolean> = kotlin.runCatching {
        val response = myCalendarService.deletePersonalSchedule(accessToken, eventId)

        if(response.dataHeader.successCode == 0) {
            true
        } else {
            throw Exception("개인 일정 삭제 오류 발생! ${response.dataHeader.resultCode} : ${response.dataHeader.resultMessage}")
        }
    }
}