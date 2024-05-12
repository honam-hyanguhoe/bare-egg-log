package com.org.egglog.presentation.domain.group.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.res.integerResource
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.domain.group.model.Admin
import com.org.egglog.domain.group.model.Group
import com.org.egglog.domain.group.model.GroupInfo
import com.org.egglog.domain.group.model.Member
import com.org.egglog.domain.group.usecase.GetGroupDutyUseCase
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
    private val groupDutyUseCase: GetGroupDutyUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel(), ContainerHost<GroupDetailState, Nothing> {
    override val container: Container<GroupDetailState, Nothing> =
        container(initialState = GroupDetailState(), buildSettings = {
            this.exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                intent {

                }
            }
        })
    val groupId = savedStateHandle.get<Long>("groupId")
        ?: throw IllegalStateException("GroupId must be provided")
    private val _selected = mutableStateOf("Day")
    val selected: MutableState<String> = _selected

    fun setSelected(newValue: String) = intent {
        reduce { state.copy(dutyType = newValue) }
        updateData()
    }

    fun updateData() = intent {
        val tempDuty: List<Member> = when (state.dutyType) {
            "Day" -> state.day
            "Eve" -> state.eve
            "Night" -> state.night
            "Off" -> state.off
            "Etc" -> state.etc
            else -> emptyList()
        }

        reduce {
            state.copy(
                selectedDuty = tempDuty
            )
        }
    }

    init {
        Log.d("groupDetail", "groupId $groupId")
        loadInit()
    }

    private fun loadInit() = intent {
        getGroupInfo(groupId = groupId)
        getGroupDuty()
    }

    private fun getGroupDuty() = intent {
        val tokens = getTokenUseCase()

        Log.d(
            "groupDetail",
            "${tokens.first} ${state.groupInfo.id} ${state.currentWeekDays.selectedDate.date.toString()}"
        )
        val result = groupDutyUseCase(
            accessToken = "Bearer ${tokens.first}",
            groupId = groupId,
            date = state.currentWeekDays.selectedDate.date.toString()
        ).getOrNull()
        Log.d("groupDetail", "getGroupDuty $result")

        reduce {
            state.copy(
                day = result?.day ?: emptyList(),
                eve = result?.eve ?: emptyList(),
                night = result?.night ?: emptyList(),
                off = result?.off ?: emptyList(),
                etc = result?.etc ?: emptyList(),
            )
        }
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

        getGroupDuty()
        updateData()
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

        getGroupDuty()
        updateData()
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
        getGroupDuty()
        updateData()
    }


    fun copyInvitationLink() = intent {
        reduce {
            state.copy(

            )
        }
    }
}


data class GroupDetailState(
    val dutyType: String = "Day",
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

    // member duty
    val day: List<Member> = emptyList(),
    val eve: List<Member> = emptyList(),
    val night: List<Member> = emptyList(),
    val off: List<Member> = emptyList(),
    val etc: List<Member> = emptyList(),
    val selectedDuty: List<Member> = emptyList(),

    // invitation
    val invitationCode : String = ""
)

