package com.org.egglog.data.main.service

import com.org.egglog.data.main.model.WeeklyWorkReponse
import com.org.egglog.data.main.model.stats.RemainDutyResponse
import com.org.egglog.data.retrofit.CommonResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface WorkService {
    @GET("work/find")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun getWeeklyWork(
        @Header("Authorization") accessToken: String,
        @Query("calendarGroupId") calendarGroupId: Long,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ) : CommonResponse<WeeklyWorkReponse?>
}