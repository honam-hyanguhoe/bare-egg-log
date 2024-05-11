package com.org.egglog.data.group.service

import com.org.egglog.data.group.model.GroupResponse
import com.org.egglog.data.retrofit.CommonResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface GroupService {

    @GET("groups/list")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun getGroupList(
        @Header("Authorization") accessToken: String,
    ): CommonResponse<List<GroupResponse>?>

    @POST("groups")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun createGroup(
        @Header("Authorization") accessToken: String,
        @Body requestBody: RequestBody
    ): CommonResponse<String>
}