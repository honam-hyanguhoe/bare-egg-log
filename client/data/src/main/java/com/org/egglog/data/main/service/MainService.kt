package com.org.egglog.data.main.service

import com.org.egglog.data.retrofit.CommonResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface MainService {
    @POST("/v1/work/find")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun getWeeklyWork(
        @Body requestBody: RequestBody
    ) : CommonResponse<String>
}