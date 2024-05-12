package com.org.egglog.domain.setting.usecase

interface SetCalendarGroupMapStoreUseCase {
    suspend operator fun invoke(map: Map<String, Boolean>)
}