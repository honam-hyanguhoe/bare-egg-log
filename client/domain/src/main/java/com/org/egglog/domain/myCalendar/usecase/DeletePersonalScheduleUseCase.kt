package com.org.egglog.domain.myCalendar.usecase

interface DeletePersonalScheduleUseCase {

    suspend operator fun invoke(accessToken: String, eventId: Int): Result<Boolean>
}