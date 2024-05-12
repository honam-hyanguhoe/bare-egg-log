package com.org.egglog.domain.setting.usecase

interface GetCalendarGroupMapStoreUseCase {
    suspend operator fun invoke(): Map<String, Boolean>
}