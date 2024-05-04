package com.org.egglog.data.community.service

import com.org.egglog.data.retrofit.CommonResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface PostingService {

    @POST("/v1/boards")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun writePost(
        @Body requestBody: RequestBody
    ) : CommonResponse<Long>
}