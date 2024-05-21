package com.org.egglog.domain.main.usecase

import com.org.egglog.domain.main.model.WorkStats

interface GetWorkStatsUseCase {
    suspend operator fun invoke (
        accessToken: String,
        date : String, // 오늘(YYYY-MM-DD)
        month  : String  // 조회할 타겟 월(YYYY-MM-DD)
    ) : Result<WorkStats?>
}