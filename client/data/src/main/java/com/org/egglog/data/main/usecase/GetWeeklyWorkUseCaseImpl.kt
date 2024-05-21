package com.org.egglog.data.main.usecase

import android.util.Log
import com.org.egglog.data.main.model.GetWeeklyWorkParam
import com.org.egglog.data.main.model.toDomainModel
import com.org.egglog.data.main.service.WorkService
import com.org.egglog.domain.main.model.WeeklyWork
import com.org.egglog.domain.main.usecase.GetWeeklyWorkUseCase
import javax.inject.Inject

class GetWeeklyWorkUseCaseImpl @Inject constructor(
    private val workService: WorkService
) : GetWeeklyWorkUseCase{
    override suspend fun invoke(accessToken: String, calendarGroupId: Long, startDate: String, endDate: String): Result<WeeklyWork?> {
        Log.d("weekly", "hi")
        return try {
            val response = workService.getWeeklyWork(accessToken, calendarGroupId, startDate, endDate)
            Log.d("weekly", "response ${response.dataBody!!.toDomainModel()}")

            if (response.dataHeader.successCode == 0) {

                Result.success(response.dataBody?.toDomainModel())
            } else {
                Result.failure(RuntimeException("Failed to fetch weekly work: ${response.dataHeader.resultCode}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}