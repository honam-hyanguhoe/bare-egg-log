package com.org.egglog.data.group.service

import com.org.egglog.data.group.model.GroupResponse
import com.org.egglog.data.retrofit.CommonResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface GroupService {

    @GET("groups/list")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun getGroupList(
        @Header("Authorization") accessToken: String,
    ): CommonResponse<List<GroupResponse>?>
}