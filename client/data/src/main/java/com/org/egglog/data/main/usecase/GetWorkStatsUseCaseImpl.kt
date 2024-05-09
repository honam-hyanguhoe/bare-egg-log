package com.org.egglog.data.main.usecase

import android.util.Log
import com.org.egglog.data.main.model.stats.toDomainModel
import com.org.egglog.data.main.model.toDomainModel
import com.org.egglog.data.main.service.StaticsService
import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.domain.main.model.WorkStats
import com.org.egglog.domain.main.usecase.GetWorkStatsUseCase
import kotlinx.serialization.json.Json
import javax.inject.Inject

class GetWorkStatsUseCaseImpl @Inject constructor(
    private val staticsService: StaticsService
) : GetWorkStatsUseCase {
    override suspend fun invoke(accessToken: String, date: String, month: String): Result<WorkStats?> = kotlin.runCatching {
        val response = staticsService.getWorkStatics(accessToken = accessToken, today = date, month = month)

        if (response.dataHeader.successCode == 0) {
            Log.d("stats", "impl 결과 ${response.dataBody}")
            Result.success(response.dataBody?.toDomainModel())
        } else {
            Result.failure(RuntimeException("Failed to fetch weekly work: ${response.dataHeader.resultCode}"))
        }
    }.getOrElse { e ->
        Result.failure(e)
    }
}
