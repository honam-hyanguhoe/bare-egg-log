package com.org.egglog.presentation.domain.myCalendar.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class ExcelListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
): ViewModel(), ContainerHost<ExcelListState, ExcelListSideEffect> {

    private val currentYear: Int = savedStateHandle.get<Int>("currentYear") ?: throw IllegalArgumentException("currentYear is required")
    private val currentMonth: Int = savedStateHandle.get<Int>("currentMonth") ?: throw IllegalArgumentException("currentMonth is required")

    override val container: Container<ExcelListState, ExcelListSideEffect> = container (
        initialState = ExcelListState(),
        buildSettings = {
            this.exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                intent { postSideEffect(ExcelListSideEffect.Toast(throwable.message.orEmpty())) }
            }
        }
    )




}

data class ExcelListState(
    val str: String = ""
)

sealed interface ExcelListSideEffect{
    class Toast(message: String): ExcelListSideEffect
}