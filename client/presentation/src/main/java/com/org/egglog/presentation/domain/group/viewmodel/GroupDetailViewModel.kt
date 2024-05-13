package com.org.egglog.presentation.domain.group.viewmodel

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.res.integerResource
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.domain.auth.usecase.GetUserStoreUseCase
import com.org.egglog.domain.group.model.Admin
import com.org.egglog.domain.group.model.Group
import com.org.egglog.domain.group.model.GroupInfo
import com.org.egglog.domain.group.model.Member
import com.org.egglog.domain.group.usecase.GetGroupDutyUseCase
import com.org.egglog.domain.group.usecase.GetGroupInfoUseCase
import com.org.egglog.domain.group.usecase.GetInvitationCodeUseCase
import com.org.egglog.domain.group.usecase.GetMembersWorkUseCase
import com.org.egglog.domain.main.model.WeeklyWork
import com.org.egglog.domain.main.usecase.GetWeeklyWorkUseCase
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
    private val getUserStoreUseCase: GetUserStoreUseCase,
    private val groupInfoUseCase: GetGroupInfoUseCase,
    private val groupDutyUseCase: GetGroupDutyUseCase,
    private val getInvitationCodeUseCase: GetInvitationCodeUseCase,
    private val getWeeklyWorkUseCase: GetWeeklyWorkUseCase,
    private val getMembersWorkUseCase: GetMembersWorkUseCase,
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
    val dataSource = WeeklyDataSource()

    init {
        Log.d("groupDetail", "groupId $groupId")
        loadInit()
    }

    private fun loadInit() = intent {
        Log.d("groupDetail", "$groupId")
        getGroupInfo(groupId = groupId)
        getGroupDuty()
        getMyWork()
    }

    fun setSelected(newValue: String) = intent {
        reduce { state.copy(dutyType = newValue) }
        updateData()
    }

    fun toggleMemberSelection(member: Member) {
        intent {
            val currentMembers = state.selectedMembers
            val newMembers = when {
                currentMembers.contains(member) -> {
                    currentMembers - member
                }

                currentMembers.size < 3 -> {
                    currentMembers + member
                }

                else -> {
                    currentMembers
                }
            }
            reduce {
                state.copy(selectedMembers = newMembers)
            }
        }
        getGroupWork()
    }

    private fun formatWork(
        weeklyWork: WeeklyWork?,
        userName: String
    ): Map<String, Map<String, String>> {
        val workMap: Map<String, String> = weeklyWork?.workList?.associate {
            it.workDate to it.workType.workTag
        }!!

        // 한 달 근무 안채워져 있으면 None으로 채우는 로직 추가

        return mapOf(userName to workMap)
    }

    fun getMyWork() = intent {
        val tokens = getTokenUseCase()
        val user = getUserStoreUseCase()

        val tempStartDate =
            dataSource.getStartOfMonth(LocalDate.now().year, LocalDate.now().monthValue)
        val tempEnddate = dataSource.getEndOfMonth(LocalDate.now().year, LocalDate.now().monthValue)

        Log.d("myWork", "$tempEnddate  $tempStartDate")
        val result = getWeeklyWorkUseCase(
            accessToken = "Bearer ${tokens.first}",
            calendarGroupId = user?.workGroupId ?: 0,
            startDate = tempStartDate.toString(),
            endDate = tempEnddate.toString(),
        ).getOrNull()

        Log.d("myWork", "result $result")

        val userName = getUserStoreUseCase()!!.userName
        val scheduleWork = formatWork(result, userName)
        Log.d("myWork", "scheduleWork $scheduleWork")

        reduce {
            state.copy(myWorkList = scheduleWork)
        }
    }


    // 같은 그룹 유저의 근무 일정 조회
    fun getGroupWork() = intent {
        val tokens = getTokenUseCase()

        val tempStartDate =
            dataSource.getStartOfMonth(LocalDate.now().year, LocalDate.now().monthValue)
        val tempEnddate = dataSource.getEndOfMonth(LocalDate.now().year, LocalDate.now().monthValue)

        val result = getMembersWorkUseCase(
            accessToken = "Bearer ${tokens.first}",
            userGroupId = groupId,
            targetUserId = state.selectedMembers[0].userId,
            startDate = tempStartDate.toString(),
            endDate = tempEnddate.toString(),
        ).getOrNull()

        Log.d("getGroupWork", "result $result")
    }


    private fun getGroupDuty() = intent {
        val tokens = getTokenUseCase()

        val result = groupDutyUseCase(
            accessToken = "Bearer ${tokens.first}",
            groupId = groupId,
            date = state.currentWeekDays.selectedDate.date.toString()
        ).getOrNull()

        reduce {
            state.copy(
                day = result?.copy()?.day ?: emptyList(),
                eve = result?.copy()?.eve ?: emptyList(),
                night = result?.copy()?.night ?: emptyList(),
                off = result?.copy()?.off ?: emptyList(),
                etc = result?.copy()?.etc ?: emptyList(),
            )
        }
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
        Log.d("invite", "selectedDuty ${state.selectedDuty}")
    }


    private fun getGroupInfo(groupId: Long) = intent {
        val tokens = getTokenUseCase()
        val result = groupInfoUseCase(
            accessToken = "Bearer ${tokens.first}",
            groupId = groupId
        ).getOrThrow()
        Log.d(
            "groupDetail", "getGroupInfo ${
                groupInfoUseCase(
                    accessToken = "Bearer ${tokens.first}",
                    groupId = groupId
                )
            }"
        )

        reduce {
            state.copy(
                groupInfo = result
            )
        }
    }


    fun onPrevClick(localDate: LocalDate) = intent {
        val finalStartDate = dataSource.getStartOfWeek(localDate)
        val calendarUiModel: WeeklyUiModel =
            dataSource.getData(
                startDate = finalStartDate.minusDays(2),
                lastSelectedDate = finalStartDate
            )
        reduce {
            state.copy(
                currentWeekDays = calendarUiModel,
                startDate = calendarUiModel.startDate.date,
                endDate = finalStartDate
            )
        }
        getGroupDuty()
    }


    fun onDateClick(date: WeeklyUiModel.Date) = intent {
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
        getGroupDuty()
    }

    fun onNextClick(localDate: LocalDate) = intent {
        val finalStartDate = dataSource.getEndOfWeek(localDate)
        val calendarUiModel: WeeklyUiModel =
            dataSource.getData(
                startDate = finalStartDate,
                lastSelectedDate = finalStartDate.plusDays(1)
            )

        reduce {
            state.copy(
                currentWeekDays = calendarUiModel,
                startDate = calendarUiModel.startDate.date,
                endDate = finalStartDate.plusDays(7)
            )
        }

        getGroupDuty()
    }


    private fun getInvitationCode() = intent {
        val tokens = getTokenUseCase()
        val result = getInvitationCodeUseCase(
            accessToken = "Bearer ${tokens.first}",
            groupId = groupId
        ).getOrNull()


        reduce {
            state.copy(
                invitationCode = result ?: ""
            )
        }

        Log.d("invitationCode" , "${state.invitationCode}")
    }

    fun copyInvitationLink(context: Context) = intent {
        Log.d("invite", "invite 시작")
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        getInvitationCode()

        // 수정 예정
        val clip = ClipData.newPlainText("label", state.invitationCode)
        clipboard.setPrimaryClip(clip)
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
    val invitationCode: String = "",

    // 선택한 멤버 근무 조회
    val selectedMembers: List<Member> = emptyList(),
    val myWorkList: Map<String, Map<String, String>> = emptyMap(),
    val groupWorkList: Map<String, Map<String, String>> = emptyMap()
)

