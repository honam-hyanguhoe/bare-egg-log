package com.org.egglog.data.scheduler.service

import com.org.egglog.data.retrofit.CommonResponse
import com.org.egglog.data.scheduler.model.AlarmFindResponse
import com.org.egglog.data.scheduler.model.PersonalScheduleResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface AlarmService {
    @GET("work/alarm-find")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun getAlarmFindList(
        @Header("Authorization") accessToken: String,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
    ): CommonResponse<AlarmFindResponse?>

    @GET("events/month")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun getPersonalList(
        @Header("Authorization") accessToken: String,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
        @Query("userId") userId: Long,
        @Query("calendarGroupId") calendarGroupId: Long
    ): CommonResponse<List<PersonalScheduleResponse>?>
}