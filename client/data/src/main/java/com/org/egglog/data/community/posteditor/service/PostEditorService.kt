package com.org.egglog.data.community.posteditor.service

import com.org.egglog.data.retrofit.CommonResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface PostEditorService {

    @POST("/v1/boards")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun writePost(
        @Header("Authorization") accessToken: String,
        @Body requestBody: RequestBody
    ) : CommonResponse<String>

    @PATCH("/v1/boards/{board_id}")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun modifyPost(
        @Header("Authorization") accessToken: String,
        @Path("board_id") boardId: Int,
        @Body requestBody: RequestBody
    ): CommonResponse<String>
}