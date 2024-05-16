package com.org.egglog.domain.myCalendar.usecase

import com.org.egglog.domain.myCalendar.model.EventListData

interface GetDetailPersonalScheduleUseCase {

    suspend operator fun invoke(accessToken: String, eventId: Int): Result<EventListData>
}