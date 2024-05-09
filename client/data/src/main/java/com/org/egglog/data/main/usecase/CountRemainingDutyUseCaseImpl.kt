package com.org.egglog.data.main.usecase

import android.util.Log
import com.org.egglog.data.main.model.stats.RemainDutyResponse
import com.org.egglog.data.main.model.stats.toDomainModel
import com.org.egglog.data.main.service.StaticsService
import com.org.egglog.domain.main.model.RemainDuty
import com.org.egglog.domain.main.usecase.CountRemainingDutyUseCase
import javax.inject.Inject

class CountRemainingDutyUseCaseImpl @Inject constructor(
    private val staticsService: StaticsService
) : CountRemainingDutyUseCase {
    override suspend fun invoke(
        accessToken: String,
        date: String,
        dateType: String
    ): Result<List<RemainDuty>> {

        return try {
            val response = staticsService.countRemainingDuty(accessToken, dateType, date)
            if (response.dataHeader.successCode == 0) {
                Log.d("stats response!!!", "DataBody: ${response.dataBody}")
                val remainDutyList = response.dataBody
                Result.success(remainDutyList!!.mapNotNull { it.toDomainModel() })
            } else {
                Log.e(
                    "stats error",
                    "Failed to fetch work stats with error code: ${response.dataHeader.resultCode}"
                )
                Result.failure(RuntimeException("Failed to fetch work stats: ${response.dataHeader.resultCode}"))
            }
        } catch (e: Exception) {
            Log.e("stats exception", "Error fetching work stats", e)
            Result.failure(e)
        }
    }
}