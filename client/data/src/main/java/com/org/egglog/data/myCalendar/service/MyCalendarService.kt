package com.org.egglog.data.myCalendar.service

import com.google.android.gms.common.internal.service.Common
import com.org.egglog.data.myCalendar.model.EventListResponse
import com.org.egglog.data.myCalendar.model.PersonalScheduleResponse
import com.org.egglog.data.myCalendar.model.WorkListResponse
import com.org.egglog.data.myCalendar.model.WorkScheduleResponse
import com.org.egglog.data.myCalendar.model.WorkTypeResponse
import com.org.egglog.data.retrofit.CommonResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MyCalendarService {

    @GET("worktypes/list")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun getWorkTypeList(
        @Header("Authorization") accessToken: String
    ): CommonResponse<List<WorkTypeResponse>>

    @POST("events")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun createPersonalSchedule(
        @Header("Authorization") accessToken: String,
        @Body requestBody: RequestBody
    ): CommonResponse<String>

    @POST("work/create")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun createWorkSchedule(
        @Header("Authorization") accessToken: String,
        @Body requestBody: RequestBody
    ): CommonResponse<List<WorkListResponse>>

    @PATCH("work/update")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun updateWorkSchedule(
        @Header("Authorization") accessToken: String,
        @Body requestBody: RequestBody
    ): CommonResponse<List<WorkListResponse>>

    @GET("work/find")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun getWorkList(
        @Header("Authorization") accessToken: String,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): CommonResponse<WorkScheduleResponse>

    @GET("events/month")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun getPersonalList(
        @Header("Authorization") accessToken: String,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
        @Query("userId") userId: Long,
        @Query("calendarGroupId") calendarGroupId: Long
    ): CommonResponse<List<PersonalScheduleResponse>>

    @DELETE("events/{event_id}")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun deletePersonalSchedule(
        @Header("Authorization") accessToken: String,
        @Path("event_id") eventId: Int
    ): CommonResponse<String>

    @GET("events/{event_id}")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun getDetailPersonalSchedule(
        @Header("Authorization") accessToken: String,
        @Path("event_id") eventId: Int
    ): CommonResponse<EventListResponse>

    @PATCH("events/{event_id}")
    @Headers("Content-Type:application/json; charset=UTF8")
    suspend fun modifyPersonalSchedule(
        @Header("Authorization") accessToken: String,
        @Path("event_id") eventId: Int,
        @Body requestBody: RequestBody
    ): CommonResponse<EventListResponse>
}