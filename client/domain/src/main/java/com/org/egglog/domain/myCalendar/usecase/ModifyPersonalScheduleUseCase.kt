package com.org.egglog.domain.myCalendar.usecase

import java.time.LocalDateTime

interface ModifyPersonalScheduleUseCase {

    suspend operator fun invoke(accessToken: String, eventId: Int, eventTitle: String, eventContent: String, startDate: LocalDateTime, endDate: LocalDateTime, calendarGroupId: Long): Result<Boolean>
}