package com.org.egglog.data.community.service

import com.org.egglog.data.community.model.CommentResponse
import com.org.egglog.data.community.model.CommunityGroupResponse
import com.org.egglog.data.community.model.HotPostResponse
import com.org.egglog.data.community.model.PostDetailResponse
import com.org.egglog.data.community.model.PostResponse
import com.org.egglog.data.retrofit.CommonResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
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
        @Query("offset") lastBoardId: Int? = 0
    ): CommonResponse<List<PostResponse?>>

    @GET("boards/{postId}")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun getPostDetail(
        @Header("Authorization") accessToken: String,
        @Path("postId") postId: Int
    ): CommonResponse<PostDetailResponse?>

    @DELETE("boards/{postId}")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun deletePost(
        @Header("Authorization") accessToken: String,
        @Path("postId") postId: Int
    ): CommonResponse<String>

    @GET("boards/types")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun getCommunityGroup(
        @Header("Authorization") accessToken: String
    ): CommonResponse<CommunityGroupResponse>

    @GET("board/{postId}/comments")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun getCommentList(
        @Header("Authorization") accessToken: String,
        @Path("postId") postId: Int
    ): CommonResponse<List<CommentResponse?>>

    @DELETE("board/comment/{commentId}")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun deleteComment(
        @Header("Authorization") accessToken: String,
        @Path("commentId") commentId: Long
    ): CommonResponse<String>

    @POST("board/comment")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun createComment(
        @Header("Authorization") accessToken: String,
        @Body requestBody: RequestBody
    ): CommonResponse<String>

}