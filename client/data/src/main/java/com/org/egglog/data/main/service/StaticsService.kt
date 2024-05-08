package com.org.egglog.data.main.service

import com.org.egglog.data.retrofit.CommonResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface StaticsService {
    @GET("work/find/upcoming/{dateType}")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun countRemainingDuty(
        @Body requestBody: RequestBody
    ) : CommonResponse<String>


    @GET("work/find/completed")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun getWorkStatics(
        @Header("Authorization") accessToken: String,
        @Query("today") today: String,
        @Query("month") month: String,
    ) : CommonResponse<String>
}