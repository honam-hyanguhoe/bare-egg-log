package com.org.egglog.domain.main.usecase

interface CountRemainingDutyUseCase {
    suspend operator fun invoke (
        accessToken: String,
        date : String, // 오늘(YYYY-MM-DD)
        dateType : String  // WEEK, MONTH
    )
}