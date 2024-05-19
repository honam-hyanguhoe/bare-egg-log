package com.org.egglog.presentation.domain.myCalendar.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.domain.myCalendar.model.ExcelData
import com.org.egglog.domain.myCalendar.usecase.GetExcelListUseCase
import com.org.egglog.domain.myCalendar.usecase.RequestWorkSyncUseCase
import com.org.egglog.domain.scheduler.usecase.GetAlarmFindListUseCase
import com.org.egglog.domain.scheduler.usecase.SchedulerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ExcelListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getTokenUseCase: GetTokenUseCase,
    private val getExcelListUseCase: GetExcelListUseCase,
    private val requestWorkSyncUseCase: RequestWorkSyncUseCase,
    private val getAlarmFindListUseCase: GetAlarmFindListUseCase,
    private val schedulerUseCase: SchedulerUseCase,
): ViewModel(), ContainerHost<ExcelListState, ExcelListSideEffect> {

    private val currentYear: Int = savedStateHandle.get<Int>("currentYear") ?: throw IllegalArgumentException("currentYear is required")
    private val currentMonth: Int = savedStateHandle.get<Int>("currentMonth") ?: throw IllegalArgumentException("currentMonth is required")
    private var accessToken: String ?= null

    override val container: Container<ExcelListState, ExcelListSideEffect> = container (
        initialState = ExcelListState(),
        buildSettings = {
            this.exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                intent { postSideEffect(ExcelListSideEffect.Toast(throwable.message.orEmpty())) }
            }
        }
    )

    init {
        load()
    }

    private fun load() = intent {
        accessToken = getTokenUseCase().first
        getExcelList()
    }

    private fun getExcelList() = intent {
        val month =
            if (currentMonth < 10) "0${currentMonth}" else "${currentMonth}"
        val date = "${currentYear}-${month}"
        val excelList = getExcelListUseCase("Bearer $accessToken", date).getOrThrow()
        Log.e("ExcelList", "$excelList")
        reduce { state.copy(excelList = excelList) }

    }

    private fun stringToNewLocalDateTime(dateTimeString: String): LocalDateTime {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm")
        return LocalDateTime.parse(dateTimeString, formatter)
    }

    fun onConfirm(index: Int) = intent {
        val excelData = state.excelList[index]
        val date = "${excelData.index.date}-01"
        val response = requestWorkSyncUseCase("Bearer $accessToken", excelData.index.groupId.toLong(), date, excelData.index.index.toLong())

        if(response.isSuccess) {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val alarmList = getAlarmFindListUseCase(
                "Bearer $accessToken",
                LocalDate.now().format(formatter),
                LocalDate.now().plusDays(8).format(formatter)
            ).getOrThrow()
            alarmList?.workList?.map {
                val dateTime = stringToNewLocalDateTime("${it.work.workDate}${it.alarm.alarmTime}")
                if(it.alarm.isAlarmOn && dateTime > LocalDateTime.now()) schedulerUseCase.setAlarm("${dateTime.monthValue - 1}${dateTime.dayOfMonth}".toInt(), 0, it.alarm.alarmReplayCnt, it.alarm.alarmReplayTime.toLong(), dateTime, false)
                else schedulerUseCase.cancelAllAlarms("${dateTime.monthValue - 1}${dateTime.dayOfMonth}".toInt())
            }
            postSideEffect(ExcelListSideEffect.Toast("동기화 되었습니다"))
            postSideEffect(ExcelListSideEffect.NavigateToCalendarActivity)
        } else {
            postSideEffect(ExcelListSideEffect.Toast("다시 시도해주세요"))
        }

    }






}

data class ExcelListState(
    val excelList: List<ExcelData> = listOf()
)

sealed interface ExcelListSideEffect{
    class Toast(val message: String): ExcelListSideEffect

    data object NavigateToCalendarActivity: ExcelListSideEffect
}