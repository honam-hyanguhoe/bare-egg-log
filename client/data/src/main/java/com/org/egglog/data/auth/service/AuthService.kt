package com.org.egglog.data.auth.service

import com.org.egglog.data.auth.model.RefreshResponse
import com.org.egglog.data.retrofit.CommonResponse
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthService {
    @POST("auth/refresh")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun refresh(
        @Header("refreshToken") refreshToken: String
    ): CommonResponse<RefreshResponse?>

    @POST("auth/logout")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun logout(
        @Header("Authorization") accessToken: String
    ): CommonResponse<Unit?>
}