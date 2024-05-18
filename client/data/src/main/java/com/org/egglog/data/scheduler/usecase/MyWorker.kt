package com.org.egglog.data.scheduler.usecase

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.domain.auth.usecase.GetUserStoreUseCase
import com.org.egglog.domain.scheduler.usecase.GetAlarmFindListUseCase
import com.org.egglog.domain.scheduler.usecase.GetAlarmPersonalScheduleUseCase
import com.org.egglog.domain.scheduler.usecase.NotificationUseCase
import com.org.egglog.domain.scheduler.usecase.SchedulerUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MyWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val getTokenUseCase: GetTokenUseCase,
    private val getUserStoreUseCase: GetUserStoreUseCase,
    private val notificationUseCase: NotificationUseCase,
    private val scheduleUseCase: SchedulerUseCase,
    private val getAlarmFindListUseCase: GetAlarmFindListUseCase,
    private val getAlarmPersonalScheduleUseCase: GetAlarmPersonalScheduleUseCase
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val data = fetchDataFromNetwork()
            Log.e("MyWorker", data)
            Result.success()
        } catch (e: Exception) {
            Log.e("MyWorker", "Error fetching data", e)
            Result.retry()
        }
    }

    private fun stringToLocalDateTime(dateTimeString: String): LocalDateTime {
        return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }

    private fun stringToNewLocalDateTime(dateTimeString: String): LocalDateTime {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm")
        return LocalDateTime.parse(dateTimeString, formatter)
    }

    private suspend fun fetchDataFromNetwork(): String {
        val tokens = getTokenUseCase()
        val userDetail = getUserStoreUseCase()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val nextDaySchedule = getAlarmPersonalScheduleUseCase(
            "Bearer ${tokens.first.orEmpty()}",
//            LocalDate.now().format(formatter),
            LocalDate.now().plusDays(1).format(formatter),
            LocalDate.now().plusDays(1).format(formatter),
            userDetail!!.id,
            userDetail.workGroupId ?: 0L
        ).getOrThrow()

        nextDaySchedule?.map {dateList ->
            dateList.eventList.map { notificationUseCase.setNotification(it.eventId, stringToLocalDateTime(it.startDate).minusMinutes(30L)) }
        }
        val alarmList = getAlarmFindListUseCase(
            "Bearer ${tokens.first.orEmpty()}",
//            LocalDate.now().format(formatter),
            LocalDate.now().plusDays(1).format(formatter),
            LocalDate.now().plusDays(8).format(formatter)
        ).getOrThrow()
        alarmList?.workList?.map {
            if(it.alarm.isAlarmOn) scheduleUseCase.setAlarm(it.work.workId, 0, it.alarm.alarmReplayCnt, it.alarm.alarmReplayTime.toLong(), stringToNewLocalDateTime("${it.work.workDate}${it.alarm.alarmTime}"), false)
        }
        return "$nextDaySchedule\n$alarmList"
    }

    companion object {
        fun createWorkRequest(): PeriodicWorkRequest {
            val currentTime = Calendar.getInstance()
            val dueTime = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                if (before(currentTime)) {
                    add(Calendar.DAY_OF_MONTH, 1)
                }
            }

            val initialDelay = dueTime.timeInMillis - currentTime.timeInMillis

            return PeriodicWorkRequestBuilder<MyWorker>(1, TimeUnit.DAYS)
                .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                .setConstraints(
                    androidx.work.Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .build()
        }
    }

    class Factory @Inject constructor(
        private val getTokenUseCase: GetTokenUseCase,
        private val getUserStoreUseCase: GetUserStoreUseCase,
        private val notificationUseCase: NotificationUseCase,
        private val scheduleUseCase: SchedulerUseCase,
        private val getAlarmFindListUseCase: GetAlarmFindListUseCase,
        private val getAlarmPersonalScheduleUseCase: GetAlarmPersonalScheduleUseCase
    ) : ChildWorkerFactory {
        override fun create(appContext: Context, workerParameters: WorkerParameters): ListenableWorker {
            return MyWorker(
                appContext,
                workerParameters,
                getTokenUseCase,
                getUserStoreUseCase,
                notificationUseCase,
                scheduleUseCase,
                getAlarmFindListUseCase,
                getAlarmPersonalScheduleUseCase
            )
        }
    }
}
