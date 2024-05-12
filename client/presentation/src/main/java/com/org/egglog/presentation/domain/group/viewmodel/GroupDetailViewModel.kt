package com.org.egglog.presentation.domain.group.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.domain.group.model.Admin
import com.org.egglog.domain.group.model.Group
import com.org.egglog.domain.group.model.GroupInfo
import com.org.egglog.domain.group.usecase.GetGroupInfoUseCase
import com.org.egglog.presentation.component.organisms.calendars.weeklyData.WeeklyDataSource
import com.org.egglog.presentation.component.organisms.calendars.weeklyData.WeeklyUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class GroupDetailViewModel @Inject constructor(
    private val getTokenUseCase: GetTokenUseCase,
    private val groupInfoUseCase: GetGroupInfoUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel(), ContainerHost<GroupDetailState, Nothing> {
    override val container: Container<GroupDetailState, Nothing> =
        container(initialState = GroupDetailState(), buildSettings = {
            this.exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                intent {

                }
            }
        })

    init {
        val groupId = savedStateHandle.get<Long>("groupId")
            ?: throw IllegalStateException("GroupId must be provided")
        Log.d("groupDetail", "groupId $groupId")
        getGroupInfo(groupId)

        loadInit()
    }

    private fun loadInit() = intent {
        Log.d("groupDetail", "시작 지점 ${state.currentWeekDays}")
        Log.d("groupDetail", "시작 지점 ${state.startDate}   ${state.endDate}")

        val initDays: WeeklyUiModel = WeeklyDataSource().getData(
            LocalDate.now().minusDays(1),
            LocalDate.now()
        )

        Log.d("groupDetail", "$initDays")

    }

    private fun getGroupInfo(groupId: Long) = intent {

        val tokens = getTokenUseCase()

        val result = groupInfoUseCase(
            accessToken = "Bearer ${tokens.first}",
            groupId = groupId
        ).getOrNull()
        Log.d("groupDetail", "getGroupInfo ${result}")

        reduce {
            state.copy(
                groupInfo = result!!
            )
        }
    }

    val dataSource = WeeklyDataSource()
    fun onPrevClick(localDate: LocalDate) = intent {
        val finalStartDate = dataSource.getStartOfWeek(localDate)
        Log.d("groupDetail", "finalStartDate ${localDate} --- ${finalStartDate}")

        val calendarUiModel: WeeklyUiModel =
            dataSource.getData(
                startDate = finalStartDate.minusDays(2),
                lastSelectedDate = finalStartDate
            )
        Log.d("groupDetail", "${calendarUiModel}")

        reduce {
            state.copy(
                currentWeekDays = calendarUiModel,
                startDate = calendarUiModel.startDate.date,
                endDate = finalStartDate
            )
        }

        Log.d("groupDetail", "${state.currentWeekDays}")
        Log.d("groupDetail", " ${state.startDate} ---- ${state.endDate}")
    }

    fun onDateClick(date: WeeklyUiModel.Date) = intent {
        Log.d("groupDetail", "onDateClick!")
        reduce {
            state.copy(
                currentWeekDays = state.currentWeekDays.copy(
                    selectedDate = date,
                    visibleDates = state.currentWeekDays.visibleDates.map {
                        it.copy(
                            isSelected = it.date.isEqual(date.date)
                        )
                    }

                )
            )
        }
        Log.d("groupDetail", "선택한 근무 ${state.currentWeekDays.selectedDate}")
    }

    fun onNextClick(localDate: LocalDate) = intent {
        val finalStartDate = dataSource.getEndOfWeek(localDate)
        Log.d("groupDetail", "next click")
        Log.d("groupDetail", "${finalStartDate}")

        val calendarUiModel: WeeklyUiModel =
            dataSource.getData(
                startDate = finalStartDate,
                lastSelectedDate = finalStartDate.plusDays(1)
            )
        Log.d("groupDetail", "${calendarUiModel}")

        reduce {
            state.copy(
                currentWeekDays = calendarUiModel,
                startDate = calendarUiModel.startDate.date,
                endDate = finalStartDate.plusDays(7)
            )
        }

        Log.d("groupDetail", "${calendarUiModel}")
        Log.d("groupDetail", "${state.startDate} ---- ${state.endDate}")

    }
}


data class GroupDetailState(
    val groupInfo: GroupInfo = GroupInfo(
        id = 0,
        isAdmin = false,
        groupImage = 1,
        groupName = "",
        admin = Admin(
            userId = null,
            groupId = 12,
            userName = "도",
            profileImgUrl = "https://ssl.pstatic.net/static/pwe/address/img_profile.png",
            isAdmin = true
        ),
        groupMembers = emptyList()
    ),

    // 주간 달력 클릭
    val currentWeekDays: WeeklyUiModel = WeeklyDataSource().getData(
        LocalDate.now().minusDays(1),
        LocalDate.now()
    ),
    val startDate: LocalDate = LocalDate.now(),
    val endDate: LocalDate = LocalDate.now(),
)

