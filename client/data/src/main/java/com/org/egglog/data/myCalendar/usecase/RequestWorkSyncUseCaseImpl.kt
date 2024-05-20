package com.org.egglog.data.myCalendar.usecase

import android.util.Log
import com.org.egglog.data.myCalendar.model.WorkSyncParam
import com.org.egglog.data.myCalendar.service.MyCalendarService
import com.org.egglog.domain.myCalendar.usecase.RequestWorkSyncUseCase
import javax.inject.Inject

class RequestWorkSyncUseCaseImpl @Inject constructor(
    private val myCalendarService: MyCalendarService
) : RequestWorkSyncUseCase {
    override suspend fun invoke(
        accessToken: String,
        groupId: Long,
        targetMonth: String,
        index: Long
    ): Result<Boolean> = kotlin.runCatching {
        val requestBody = WorkSyncParam(
            groupId, targetMonth, index
        ).toRequestBody()

        val response = myCalendarService.requestWorkSync(accessToken, requestBody)

        if(response.dataHeader.successCode == 0) {
            Log.e("ExcelList", "연동 결과 $response")
            true
        } else {
            Log.e("ExcelList", "${response.dataHeader.resultMessage}")
            throw Exception("근무 동기화 에러 발생! ${response.dataHeader.resultCode}: ${response.dataHeader.resultMessage}")
        }
    }
}