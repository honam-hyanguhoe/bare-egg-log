package com.org.egglog.domain.main.usecase

import com.org.egglog.domain.main.model.WeeklyWork

interface GetWeeklyWorkUseCase {
    suspend operator fun invoke (
        accessToken: String,
        calendarGroupId: Long,
        startDate : String,
        endDate : String
    ) : Result<WeeklyWork?>
}