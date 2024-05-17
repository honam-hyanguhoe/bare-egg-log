package com.org.egglog.domain.myCalendar.usecase

interface RequestWorkSyncUseCase {

    suspend operator fun invoke(accessToken: String, groupId: Long, targetMonth: String, index: Long): Result<Boolean>
}