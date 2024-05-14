package com.org.egglog.domain.myCalendar.usecase

import com.org.egglog.domain.myCalendar.model.AddWorkData

interface CreateWorkScheduleUseCase {

    suspend operator fun invoke(accessToken: String, calendarGroupId: Long, workTypes: List<AddWorkData>): Result<Boolean>
}