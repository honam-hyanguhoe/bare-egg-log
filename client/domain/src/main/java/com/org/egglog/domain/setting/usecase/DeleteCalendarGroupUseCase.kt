package com.org.egglog.domain.setting.usecase

interface DeleteCalendarGroupUseCase {
    suspend operator fun invoke(calendarGroupId: Long, accessToken: String): Result<Unit?>
}