package com.org.egglog.data.myCalendar.usecase

import com.org.egglog.data.myCalendar.model.toDomainModel
import com.org.egglog.data.myCalendar.service.MyCalendarService
import com.org.egglog.domain.myCalendar.model.WorkScheduleData
import com.org.egglog.domain.myCalendar.usecase.GetWorkListUseCase
import javax.inject.Inject

class GetWorkListUseCaseImpl @Inject constructor(
    private val myCalendarService: MyCalendarService
): GetWorkListUseCase {
    override suspend fun invoke(
        accessToken: String,
        startDate: String,
        endDate: String
    ): Result<WorkScheduleData> = kotlin.runCatching {
        myCalendarService.getWorkList(accessToken, startDate, endDate).dataBody!!.toDomainModel()
    }
}