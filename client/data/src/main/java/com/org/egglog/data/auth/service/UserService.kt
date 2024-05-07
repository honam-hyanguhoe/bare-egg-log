package com.org.egglog.data.auth.service

import com.org.egglog.data.auth.model.RefreshResponse
import com.org.egglog.data.auth.model.UserHospitalResponse
import com.org.egglog.data.auth.model.UserResponse
import com.org.egglog.data.retrofit.CommonResponse
import com.org.egglog.domain.auth.model.UserHospital
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
        @Path("provider") type: String,
        @Body requestBody: RequestBody
    ): CommonResponse<RefreshResponse?>

    @GET("user/find")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun findUser(
        @Header("Authorization") accessToken: String
    ): CommonResponse<UserResponse?>

    @PATCH("user/join")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun join(
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