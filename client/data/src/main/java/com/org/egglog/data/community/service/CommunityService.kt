package com.org.egglog.data.community.service

import com.org.egglog.data.community.model.HotPostResponse
import com.org.egglog.data.retrofit.CommonResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface CommunityService {

    @GET("boards/hot")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun getHotPostList(
        @Header("Authorization") accessToken: String
    ): CommonResponse<List<HotPostResponse>>
}