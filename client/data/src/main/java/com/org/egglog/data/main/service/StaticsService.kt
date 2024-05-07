package com.org.egglog.data.main.service

import com.org.egglog.data.retrofit.CommonResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface StaticsService {
    @POST("/v1/work/find/upcoming/{dateType}")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun countRemainingDuty(
        @Body requestBody: RequestBody
    ) : CommonResponse<String>


    @POST("/v1/work/find/completed")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun getWorkStatics(
        @Body requestBody: RequestBody
    ) : CommonResponse<String>
}