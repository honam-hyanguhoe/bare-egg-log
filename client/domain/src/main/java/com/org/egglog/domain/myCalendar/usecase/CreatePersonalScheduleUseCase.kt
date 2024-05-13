package com.org.egglog.domain.myCalendar.usecase

import java.time.LocalDateTime

interface CreatePersonalScheduleUseCase {

    suspend operator fun invoke(
        accessToken: String,
        eventTitle: String,
        eventContent: String,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        calendarGroupId: Long
    ): Result<Boolean>
}