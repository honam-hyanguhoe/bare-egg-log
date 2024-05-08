package com.org.egglog.data.main.usecase

import com.org.egglog.domain.main.usecase.GetWorkStatsUseCase
import javax.inject.Inject

class GetWorkStatsUseCaseImpl @Inject constructor() : GetWorkStatsUseCase{
    override suspend fun invoke(date: String, month: String) {
        TODO("Not yet implemented")
    }
}