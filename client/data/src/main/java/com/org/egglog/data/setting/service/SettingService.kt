package com.org.egglog.data.setting.service

import com.org.egglog.data.retrofit.CommonResponse
import com.org.egglog.data.setting.model.CalendarGroupResponse
import com.org.egglog.data.setting.model.WorkTypeResponse
import com.org.egglog.domain.setting.model.CalendarGroup
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
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
}