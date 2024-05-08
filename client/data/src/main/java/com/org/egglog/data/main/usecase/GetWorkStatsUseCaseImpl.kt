package com.org.egglog.data.main.usecase

import android.util.Log
import com.org.egglog.data.main.model.toDomainModel
import com.org.egglog.data.main.service.StaticsService
import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.domain.main.model.WorkStats
import com.org.egglog.domain.main.usecase.GetWorkStatsUseCase
import javax.inject.Inject

class GetWorkStatsUseCaseImpl @Inject constructor(
    private val staticsService: StaticsService
) : GetWorkStatsUseCase{
    override suspend fun invoke(accessToken : String, date: String, month: String): Result<WorkStats> {


        return try {
            val response = staticsService.getWorkStatics(accessToken = accessToken, today = date, month = month)
//            Log.d("weekly", "response ${response.dataBody!!.toDomainModel()}")

            if (response.dataHeader.successCode == 0) {

//                Result.success(response.dataBody?.toDomainModel())
            } else {
                Result.failure(RuntimeException("Failed to fetch weekly work: ${response.dataHeader.resultCode}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }



    }

}