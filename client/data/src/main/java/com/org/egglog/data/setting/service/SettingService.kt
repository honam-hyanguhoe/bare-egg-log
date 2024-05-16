package com.org.egglog.data.setting.service

import com.org.egglog.data.retrofit.CommonResponse
import com.org.egglog.data.setting.model.AlarmResponse
import com.org.egglog.data.setting.model.AlarmUpdateResponse
import com.org.egglog.data.setting.model.CalendarGroupResponse
import com.org.egglog.data.setting.model.WorkTypeResponse
import com.org.egglog.domain.setting.model.CalendarGroup
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface SettingService {
    @GET("calendargroups/find/list")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun getCalendarGroupList(
        @Header("Authorization") accessToken: String
    ): CommonResponse<List<CalendarGroupResponse>?>

    @DELETE("calendargroups/delete/{calendarGroupId}")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun deleteCalendarGroup(
        @Path("calendarGroupId") calendarGroupId: Long,
        @Header("Authorization") accessToken: String
    ): CommonResponse<Unit?>

    @POST("calendargroups/create")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun postCalendarGroup(
        @Header("Authorization") accessToken: String,
        @Body requestBody: RequestBody
    ): CommonResponse<CalendarGroupResponse?>

    @POST("calendar/sync")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun postCalendarSync(
        @Header("Authorization") accessToken: String
    ): CommonResponse<Unit?>

    @GET("worktypes/list")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun getWorkTypeList(
        @Header("Authorization") accessToken: String
    ): CommonResponse<List<WorkTypeResponse>?>

    @POST("worktypes/create")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun postWorkType(
        @Header("Authorization") accessToken: String,
        @Body requestBody: RequestBody
    ): CommonResponse<WorkTypeResponse?>

    @DELETE("worktypes/{workTypeId}")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun deleteWorkType(
        @Header("Authorization") accessToken: String,
        @Path("workTypeId") workTypeId: Long
    ): CommonResponse<Unit?>

    @PATCH("worktypes/edit/{workTypeId}")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun putWorkType(
        @Header("Authorization") accessToken: String,
        @Path("workTypeId") workTypeId: Long,
        @Body requestBody: RequestBody
    ): CommonResponse<WorkTypeResponse?>

    @POST("inquiries")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun postAsk(
        @Header("Authorization") accessToken: String,
        @Body requestBody: RequestBody
    ): CommonResponse<Unit?>

    @GET("alarms")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun getAlarmList(
        @Header("Authorization") accessToken: String
    ): CommonResponse<List<AlarmResponse>?>

    @PATCH("alarms/status")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun updateAlarmStatus(
        @Header("Authorization") accessToken: String,
        @Body requestBody: RequestBody
    ): CommonResponse<AlarmUpdateResponse?>
}