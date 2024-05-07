package com.org.egglog.domain.main.usecase

interface GetWeeklyWorkUseCase {
    suspend operator fun invoke (
        startDate : String,
        endDate : String
    )
}