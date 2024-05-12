package com.org.egglog.domain.setting.usecase

import com.org.egglog.domain.setting.model.AddCalendarGroupParam
import com.org.egglog.domain.setting.model.CalendarGroup


interface PostCalendarGroupUseCase {
    suspend operator fun invoke(accessToken: String, addCalendarGroupParam: AddCalendarGroupParam): Result<CalendarGroup?>
}