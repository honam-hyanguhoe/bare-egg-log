package com.org.egglog.data.myCalendar.usecase

import com.org.egglog.data.myCalendar.model.EditWorkScheduleParam
import com.org.egglog.data.myCalendar.service.MyCalendarService
import com.org.egglog.domain.myCalendar.model.EditWorkData
import com.org.egglog.domain.myCalendar.usecase.EditWorkScheduleUseCase
import javax.inject.Inject

class EditWorkScheduleUseCaseImpl @Inject constructor(
    private val myCalendarService: MyCalendarService
): EditWorkScheduleUseCase {
    override suspend fun invoke(
        accessToken: String,
        calendarGroupId: Long,
        editWorkList: List<EditWorkData>
    ): Result<Boolean> = kotlin.runCatching {

        val requestBody = EditWorkScheduleParam(
            calendarGroupId, editWorkList
        ).toRequestBody()

        val response = myCalendarService.updateWorkSchedule(accessToken, requestBody)

        if(response.dataHeader.successCode == 0) {
            true
        } else {
            throw Exception("근무 일정 등록 에러 발생 ${response.dataHeader.resultCode} : ${response.dataHeader.resultMessage}")
        }
    }
}