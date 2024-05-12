package com.org.egglog.domain.setting.usecase

import com.org.egglog.domain.setting.model.CalendarGroup

interface GetCalendarGroupListUseCase {
    suspend operator fun invoke(accessToken: String): Result<List<CalendarGroup>?>
}