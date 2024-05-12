package com.org.egglog.data.group.service

import com.org.egglog.data.group.model.GroupDutyResponse
import com.org.egglog.data.group.model.GroupInfoResponse
import com.org.egglog.data.group.model.GroupResponse
import com.org.egglog.data.main.model.stats.RemainDutyResponse
import com.org.egglog.data.retrofit.CommonResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

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

    @GET("groups/{groupId}")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun getGroupInfo(
        @Header("Authorization") accessToken: String,
        @Path("groupId") groupId: Long,
    ): CommonResponse<GroupInfoResponse>

    @GET("groups/duty/{groupId}")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun getGroupDuty(
        @Header("Authorization") accessToken: String,
        @Path("groupId") groupId: Long,
        @Query("date") date : String
    ): CommonResponse<GroupDutyResponse?>


    // invitation
    @GET("groups/invitaion/{groupId}")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun getInvitationCode(
        @Header("Authorization") accessToken: String,
        @Path("groupId") groupId: Long,
    ): CommonResponse<String?>

    @POST("groups/invitation/accept")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun inviteMember(
        @Header("Authorization") accessToken: String,
        @Body requestBody: RequestBody
    ): CommonResponse<String?>
}

