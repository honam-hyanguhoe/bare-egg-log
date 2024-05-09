package com.org.egglog.data.auth.service

import com.org.egglog.data.auth.model.TokenResponse
import com.org.egglog.data.auth.model.UserHospitalResponse
import com.org.egglog.data.auth.model.UserResponse
import com.org.egglog.data.retrofit.CommonResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService {
    @POST("auth/login/{provider}")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun login(
        @Header("accessToken") authToken: String,
        @Path("provider") provider: String
    ): CommonResponse<TokenResponse?>

    @GET("user/find")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun findUser(
        @Header("Authorization") accessToken: String
    ): CommonResponse<UserResponse?>

    @PATCH("user/join")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun join(
        @Header("Authorization") accessToken: String,
        @Body requestBody: RequestBody
    ): CommonResponse<UserResponse?>

    @PATCH("user/fcm-modify")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun updateFcmToken(
        @Header("Authorization") accessToken: String,
        @Body requestBody: RequestBody
    ): CommonResponse<UserResponse?>

    @PATCH("user/delete")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun updateUserDelete(
        @Header("Authorization") accessToken: String
    ): CommonResponse<UserResponse?>

    @PATCH("user/info-modify")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun updateUserModify(
        @Header("Authorization") accessToken: String,
        @Body requestBody: RequestBody
    ): CommonResponse<UserResponse?>

    @GET("hospital/list")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun getHospitals(
        @Query("offset") page: Int,
        @Query("limit") size: Int,
        @Query("hospitalName") search: String
    ): CommonResponse<List<UserHospitalResponse>>
}