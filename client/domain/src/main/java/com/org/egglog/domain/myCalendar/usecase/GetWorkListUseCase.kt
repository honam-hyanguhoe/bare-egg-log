package com.org.egglog.domain.myCalendar.usecase

import com.org.egglog.domain.myCalendar.model.WorkScheduleData

interface GetWorkListUseCase {

    suspend operator fun invoke(accessToken: String, startDate: String, endDate: String): Result<WorkScheduleData>
}