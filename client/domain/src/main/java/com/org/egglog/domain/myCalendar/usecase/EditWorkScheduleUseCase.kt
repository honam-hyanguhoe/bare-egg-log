package com.org.egglog.domain.myCalendar.usecase

import com.org.egglog.domain.myCalendar.model.EditWorkData

interface EditWorkScheduleUseCase {

    suspend operator fun invoke(accessToken: String,calendarGroupId: Long, editWorkList: List<EditWorkData>): Result<Boolean>
}