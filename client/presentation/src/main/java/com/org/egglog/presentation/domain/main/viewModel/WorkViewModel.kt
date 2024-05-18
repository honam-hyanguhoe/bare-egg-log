package com.org.egglog.presentation.domain.main.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.domain.auth.usecase.GetUserStoreUseCase
import com.org.egglog.domain.main.usecase.GetWeeklyWorkUseCase
import com.org.egglog.domain.scheduler.usecase.WorkerUseCase
import com.org.egglog.presentation.component.organisms.calendars.weeklyData.WeeklyDataSource
import com.org.egglog.presentation.component.organisms.calendars.weeklyData.WeeklyUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class WorkViewModel @Inject constructor(
    private val getWeeklyWorkUseCase: GetWeeklyWorkUseCase,
    private val getTokenUseCase: GetTokenUseCase,
    private val getUserStoreUseCase: GetUserStoreUseCase,
    private val workerUseCase: WorkerUseCase
) : ViewModel(), ContainerHost<WorkState, WorkSideEffect> {
    override val container: Container<WorkState, WorkSideEffect> = container(
        initialState = WorkState(),
        buildSettings = {})

    val dataSource = WeeklyDataSource()

    init {
        intent {
            onPrevClick(state.startDate)
        }
        workerUseCase.workerDailyWork()
    }

    private suspend fun loadWork(start: String, end: String) = intent {
        Log.d("temp", "start")
        val tokens = getTokenUseCase()
        val user = getUserStoreUseCase()

        Log.d("token", "${tokens.first}")
        val response = getWeeklyWorkUseCase(
            accessToken = "Bearer ${tokens.first}",
            calendarGroupId = user?.workGroupId ?: 0,
            startDate = start,
            endDate = end
        )
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        Log.d("temp", "-- $response  $start $end")
        val tempLabels = mutableListOf<String>()

        val weeklyWork = response.getOrNull()
        if (weeklyWork != null) {
            for (i in 0 until 7) {
                val currentDate = stringToLocalDate(start).plusDays(i.toLong())

                if (currentDate.isAfter(stringToLocalDate(end))) break
                val workForDate = weeklyWork.workList.find { work ->
                    work.workDate.let { LocalDate.parse(it, dateFormatter) } == currentDate
                }

                Log.d("temp", workForDate.toString())
                if (workForDate != null) {
                    tempLabels.add(workForDate.workType!!.workTag)
                } else {
                    tempLabels.add("NONE")
                }
            }
            reduce {
                state.copy(
                    labels = tempLabels
                )
            }
        }
    }

    fun stringToLocalDate(dateString: String, pattern: String = "yyyy-MM-dd"): LocalDate {
        val dateFormatter = DateTimeFormatter.ofPattern(pattern)
        return LocalDate.parse(dateString, dateFormatter)
    }


    fun onPrevClick(clickedDate: LocalDate) = intent {
        val finalStartDate = dataSource.getStartOfWeek(clickedDate)
        val calendarUiModel =
            dataSource.getData(startDate = finalStartDate, lastSelectedDate = finalStartDate)

        Log.d("weekly", "onPrevClick  ${clickedDate} --- ${finalStartDate}")
        Log.d("weekly", "onPrevClick ${calendarUiModel}")

        loadWork(calendarUiModel.startDate.date.toString(), finalStartDate.plusDays(6).toString())

        reduce {
            state.copy(
                currentWeekDays = calendarUiModel,
                startDate = calendarUiModel.startDate.date,
                endDate = finalStartDate
            )
        }
        Log.d("weekly", "${state.currentWeekDays}")
        Log.d("weekly", " ${state.startDate} ---- ${state.endDate}")
    }

    fun onNextClick(localDate: LocalDate) = intent {
        val finalStartDate = dataSource.getEndOfWeek(localDate).plusDays(2)
        val calendarUiModel = dataSource.getData(
            startDate = finalStartDate,
            lastSelectedDate = finalStartDate.minusDays(1)
        )

        loadWork(calendarUiModel.startDate.date.toString(), finalStartDate.plusDays(5).toString())
        reduce {
            state.copy(
                currentWeekDays = calendarUiModel,
                startDate = calendarUiModel.startDate.date,
                endDate = finalStartDate.plusDays(5)
            )
        }
    }
}

data class WorkState(
    val currentWeekDays: WeeklyUiModel = WeeklyDataSource().getData(
        LocalDate.now(),
        LocalDate.now()
    ),
    val startDate: LocalDate = LocalDate.now(),
    val endDate: LocalDate = LocalDate.now(),
    val labels: List<String> = listOf("NONE", "NONE", "NONE", "NONE", "NONE", "NONE", "NONE"),
)

sealed class WorkSideEffect {
    class Toast(val message: String) : WorkSideEffect()
    object NavigateToCalendarScreen : WorkSideEffect()
    object NavigateToGroupScreen : WorkSideEffect()
    object NavigateToHomeScreen : WorkSideEffect()
    object NavigateToCommunityScreen : WorkSideEffect()
    object NavigateToSettingScreen : WorkSideEffect()
}


