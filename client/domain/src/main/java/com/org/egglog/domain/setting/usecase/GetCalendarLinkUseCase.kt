package com.org.egglog.domain.setting.usecase

interface GetCalendarLinkUseCase {

    suspend operator fun invoke(accessToken: String): Result<String>
}