package com.org.egglog.data.myCalendar.usecase

import com.org.egglog.data.myCalendar.model.WorkScheduleParam
import com.org.egglog.data.myCalendar.service.MyCalendarService
import com.org.egglog.domain.myCalendar.model.AddWorkData
import com.org.egglog.domain.myCalendar.usecase.CreateWorkScheduleUseCase
import javax.inject.Inject

class CreateWorkScheduleUseCaseImpl @Inject constructor(
    private val myCalendarService: MyCalendarService
): CreateWorkScheduleUseCase {


    override suspend fun invoke(
        accessToken: String,
        calendarGroupId: Long,
        workTypes: List<AddWorkData>
    ): Result<Boolean>  = kotlin.runCatching {

        val requestBody = WorkScheduleParam(calendarGroupId, workTypes).toRequestBody()

        val response = myCalendarService.createWorkSchedule(accessToken, requestBody)

        if(response.dataHeader.successCode == 0){
            true
        } else {
            throw Exception("근무 일정 등록 에러 발생 ${response.dataHeader.resultCode} : ${response.dataHeader.resultMessage}")
        }
    }


}