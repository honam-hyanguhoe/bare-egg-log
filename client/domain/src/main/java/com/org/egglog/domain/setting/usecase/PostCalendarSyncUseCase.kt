package com.org.egglog.domain.setting.usecase

interface PostCalendarSyncUseCase {
    suspend operator fun invoke(accessToken: String): Result<Unit?>
}