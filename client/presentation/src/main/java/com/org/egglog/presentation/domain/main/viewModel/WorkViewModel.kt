package com.org.egglog.presentation.domain.main.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.domain.auth.usecase.GetUserStoreUseCase
import com.org.egglog.domain.main.usecase.GetWeeklyWorkUseCase
import com.org.egglog.presentation.component.organisms.calendars.weeklyData.WeeklyDataSource
import com.org.egglog.presentation.component.organisms.calendars.weeklyData.WeeklyUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class WorkViewModel @Inject constructor(
    private val getWeeklyWorkUseCase: GetWeeklyWorkUseCase,
    private val getTokenUseCase: GetTokenUseCase,
    private val getUserStoreUseCase: GetUserStoreUseCase,
) : ViewModel(), ContainerHost<WorkState, WorkSideEffect> {
    override val container: Container<WorkState, WorkSideEffect> = container(
        initialState = WorkState(),
        buildSettings = {})

    init {
        getInitalData(LocalDate.now())
        loadWork()
    }

    private fun loadWork() = intent {
        Log.d("weekly", "근무 일정 초기화")
        val tokens = getTokenUseCase()
        val user = getUserStoreUseCase()

        val response = getWeeklyWorkUseCase(
            accessToken = tokens?.first ?: "",
            calendarGroupId = user?.workGroupId ?: 0,
            startDate = state.startDate.toString(),
            endDate = state.endDate.toString()
        )

        val weeklyWork = response.getOrNull()
        if (weeklyWork != null) {
            Log.d("weekly", "viewModel-response ${weeklyWork}")
            val tempLabels = weeklyWork.workList.map { it.workType.title }

            val immutableList: List<String> = ensureSevenItems(tempLabels)
            reduce {
                state.copy(
                    labels = immutableList
                )
            }
        }
    }

    fun ensureSevenItems(list: List<String>): List<String> {
        val requiredSize = 7
        val currentSize = list.size
        val itemsToAdd = requiredSize - currentSize

        // itemsToAdd가 0보다 클 때만 "none"을 추가
        return if (itemsToAdd > 0) {
            list + List(itemsToAdd) { "none" }  // 기존 리스트에 "none"을 추가한 새 리스트를 생성
        } else {
            list
        }
    }


    val dataSource = WeeklyDataSource()

    fun getInitalData(localDate: LocalDate) = intent {
        val finalStartDate = dataSource.getStartOfWeek(localDate).minusDays(1)
        val calendarUiModel: WeeklyUiModel =
            dataSource.getData(startDate = finalStartDate, lastSelectedDate = finalStartDate)
        reduce {
            state.copy(
                currentWeekDays = calendarUiModel,
                startDate = calendarUiModel.startDate.date,
                endDate = finalStartDate
            )
        }
    }

    fun onPrevClick(localDate: LocalDate) = intent {
        val finalStartDate = dataSource.getStartOfWeek(localDate).minusDays(1)
        Log.d("weekly", "finalStartDate ${localDate} --- ${finalStartDate}")

        val calendarUiModel: WeeklyUiModel =
            dataSource.getData(startDate = finalStartDate, lastSelectedDate = finalStartDate)
        Log.d("weekly", "${calendarUiModel}")


        reduce {
            state.copy(
                currentWeekDays = calendarUiModel,
                startDate = calendarUiModel.startDate.date,
                endDate = finalStartDate
            )
        }

        Log.d("weekly", "${state.currentWeekDays}")
        Log.d("weekly", " ${state.startDate} ---- ${state.endDate}")
        loadWork()
    }

    fun onNextClick(localDate: LocalDate) = intent {

        val finalStartDate = dataSource.getEndOfWeek(localDate)
        Log.d("weekly", "${finalStartDate}")

        val calendarUiModel: WeeklyUiModel =
            dataSource.getData(
                startDate = finalStartDate.plusDays(7),
                lastSelectedDate = finalStartDate.plusDays(1)
            )
        Log.d("weekly", "${calendarUiModel}")

        reduce {
            state.copy(
                currentWeekDays = calendarUiModel,
                startDate = calendarUiModel.startDate.date,
                endDate = finalStartDate.plusDays(7)
            )
        }

        Log.d("weekly", "${calendarUiModel}")
        Log.d("weekly", "${state.startDate} ---- ${state.endDate}")
        loadWork()
    }
}

data class WorkState(
    val currentWeekDays: WeeklyUiModel = WeeklyDataSource().getData(
        LocalDate.now().minusDays(1),
        LocalDate.now()
    ),
    val startDate: LocalDate = LocalDate.now(),
    val endDate: LocalDate = LocalDate.now(),
    val labels: List<String> = listOf("NIGHT", "NIGHT", "NIGHT", "NIGHT", "NIGHT", "NIGHT", "NIGHT")
)

sealed class WorkSideEffect {
    class Toast(val message: String) : WorkSideEffect()
    object NavigateToCalendarScreen : WorkSideEffect()
    object NavigateToGroupScreen : WorkSideEffect()
    object NavigateToHomeScreen : WorkSideEffect()
    object NavigateToCommunityScreen : WorkSideEffect()
    object NavigateToSettingScreen : WorkSideEffect()
}


//val dataSource = WeeklyDataSource()
//var calendarUiModel by remember { mutableStateOf(dataSource.getData(lastSelectedDate = dataSource.today)) }
//
// 마지막
//val onPrevClick: (LocalDate) -> Unit = { startDate ->
//    val finalStartDate = startDate.minusDays(1)
//    calendarUiModel = dataSource.getData(startDate = finalStartDate, lastSelectedDate = finalStartDate)
//    println("선택한 날짜는 ${finalStartDate}")}


// 시작일
//val onNextClick: (LocalDate) -> Unit = { endDate ->
//    val finalStartDate = endDate.plusDays(2)
//    calendarUiModel = dataSource.getData(startDate = finalStartDate, lastSelectedDate = finalStartDate.minusDays(1))
//    println("선택한 날짜는 ${finalStartDate.minusDays(1)}")
//}