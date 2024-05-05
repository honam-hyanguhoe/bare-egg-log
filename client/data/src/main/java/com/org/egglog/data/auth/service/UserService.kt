package com.org.egglog.data.auth.service

import com.org.egglog.data.auth.model.RefreshResponse
import com.org.egglog.data.retrofit.CommonResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface UserService {
    @GET("login/oauth2/code")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun login(
        @Query("type") type: String
    ): CommonResponse<RefreshResponse?>
}