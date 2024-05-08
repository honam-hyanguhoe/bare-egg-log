package com.org.egglog.domain.main.usecase

interface GetWorkStatsUseCase {
    suspend operator fun invoke (
        date : String, // 오늘(YYYY-MM-DD)
        month  : String  // 조회할 타겟 월(YYYY-MM-DD)
    )
}