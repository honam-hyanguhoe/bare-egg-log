package com.org.egglog.data.main.usecase

import android.util.Log
import com.org.egglog.data.main.model.stats.toDomainModel
import com.org.egglog.data.main.model.toDomainModel
import com.org.egglog.data.main.service.StaticsService
import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.domain.main.model.WorkStats
import com.org.egglog.domain.main.usecase.GetWorkStatsUseCase
import javax.inject.Inject

class GetWorkStatsUseCaseImpl @Inject constructor(
    private val staticsService: StaticsService
) : GetWorkStatsUseCase{
    override suspend fun invoke(accessToken: String, date: String, month: String): Result<WorkStats?> {
        Log.d("stats", "Date: $date, Month: $month")
        return try {
            val response = staticsService.getWorkStatics(accessToken = accessToken, today = date, month = month)
            if (response.dataHeader.successCode == 0) {
                Log.d("stats response!!!", "DataBody: ${response.dataBody}")

                val workStats = null
                Result.success(workStats)
            } else {
                Log.e("stats error", "Failed to fetch work stats with error code: ${response.dataHeader.resultCode}")
                Result.failure(RuntimeException("Failed to fetch work stats: ${response.dataHeader.resultCode}"))
            }
        } catch (e: Exception) {
            Log.e("stats exception", "Error fetching work stats", e)
            Result.failure(e)
        }
    }
}
