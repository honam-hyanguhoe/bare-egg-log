package com.org.egglog.data.community.service

import com.org.egglog.data.community.model.HotPostResponse
import com.org.egglog.data.community.model.PostResponse
import com.org.egglog.data.retrofit.CommonResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface CommunityService {

    @GET("boards/hot")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun getHotPostList(
        @Header("Authorization") accessToken: String
    ): CommonResponse<List<HotPostResponse?>>

    @GET("boards")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun getPostLIst(
        @Header("Authorization") accessToken: String,
        @Query("hospital_id") hospitalId: Int? = null,
        @Query("group_id") groupId: Int? = null,
        @Query("search_word") searchWord: String? = null,
        @Query("last_board_id") lastBoardId: Int? = null
    ): CommonResponse<List<PostResponse?>>

}