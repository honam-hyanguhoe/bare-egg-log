package com.org.egglog.presentation.domain.myCalendar.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.domain.myCalendar.model.ExcelData
import com.org.egglog.domain.myCalendar.usecase.GetExcelListUseCase
import com.org.egglog.domain.myCalendar.usecase.RequestWorkSyncUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class ExcelListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getTokenUseCase: GetTokenUseCase,
    private val getExcelListUseCase: GetExcelListUseCase,
    private val requestWorkSyncUseCase: RequestWorkSyncUseCase
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
        reduce { state.copy(excelList = excelList) }

    }

    fun onConfirm(index: Int) = intent {
        val excelData = state.excelList[index]
        val response = requestWorkSyncUseCase("Bearer $accessToken", excelData.index.groupId.toLong(), excelData.date, excelData.index.index.toLong())

        if(response.isSuccess) {
            postSideEffect(ExcelListSideEffect.Toast("동기화 되었습니다"))
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
}