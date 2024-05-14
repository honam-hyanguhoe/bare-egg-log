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
import com.org.egglog.domain.group.usecase.UpdateGroupInfoUseCase
import com.org.egglog.domain.main.model.WeeklyWork
import com.org.egglog.domain.main.model.Work
import com.org.egglog.domain.main.usecase.GetWeeklyWorkUseCase
import com.org.egglog.presentation.component.organisms.calendars.weeklyData.WeeklyDataSource
import com.org.egglog.presentation.component.organisms.calendars.weeklyData.WeeklyUiModel
import com.org.egglog.presentation.domain.auth.viewmodel.PlusLoginSideEffect
import com.org.egglog.presentation.domain.community.viewmodel.PostDetailSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.syntax.simple.blockingIntent
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
    private val updateGroupInfoUseCase: UpdateGroupInfoUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel(), ContainerHost<GroupDetailState, GroupDetailSideEffect> {
    override val container: Container<GroupDetailState, GroupDetailSideEffect> =
        container(initialState = GroupDetailState(), buildSettings = {
            this.exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                intent { postSideEffect(GroupDetailSideEffect.Toast(throwable.message.orEmpty())) }
            }
        })
    val groupId = savedStateHandle.get<Long>("groupId")
        ?: throw IllegalStateException("GroupId must be provided")
    private val _selected = mutableStateOf("Day")
    val selected: MutableState<String> = _selected
    val dataSource = WeeklyDataSource()
    private val _showBottomSheet = MutableStateFlow(false)
    val showBottomSheet: StateFlow<Boolean> = _showBottomSheet.asStateFlow()

    init {
        loadInit()
    }

    private fun loadInit() = intent {
        getGroupInfo(groupId = groupId)
//        getGroupDuty()
//        getMyWork()
//        initSelectedMembers()
    }

    fun initSelectedMembers() = intent {
        reduce {
            state.copy(
                selectedMembers = emptyList(),
                groupWorkList = emptyMap(),
            )
        }
    }


    fun setShowBottomSheet(show: Boolean) {
        _showBottomSheet.value = show
    }

    fun setSelected(newValue: String) = intent {
        reduce { state.copy(dutyType = newValue) }
        updateData()
    }


    // 멤버 선택 근무 일정 조회
    fun toggleMemberSelection(member: Member) {
        intent {
            val currentMembers = state.selectedMembers
            val currentGroupWorkList = state.groupWorkList

            val newMembers: List<Member>
            val newGroupWorkList: Map<String, Map<String, String>>

            if (currentMembers.contains(member)) {
                newMembers = currentMembers - member
                newGroupWorkList = currentGroupWorkList - member.userName.toString()
            } else if (currentMembers.size < 3) {
                newMembers = currentMembers + member
                newGroupWorkList =
                    currentGroupWorkList + getGroupWork(member.userId, member.userName)
            } else {
                newMembers = currentMembers
                newGroupWorkList = currentGroupWorkList
            }
            reduce {
                state.copy(selectedMembers = newMembers, groupWorkList = newGroupWorkList)
            }
        }
    }

    private fun formatWork(
        weeklyWork: WeeklyWork?,
        userName: String
    ): Map<String, Map<String, String>> {
        val workMap: Map<String, String> = weeklyWork?.workList?.associate {
            it.workDate to it.workType.workTag
        }!!

        return mapOf(userName to workMap)
    }

    private fun formatGroupMembersWork(
        groupMemberWork: List<Work>,
        userName: String
    ): Map<String, Map<String, String>> {
        val workMap: Map<String, String> =
            groupMemberWork.associate { it.workDate to it.workType.workTag }

        return mapOf(userName to workMap)
    }

    fun getMyWork() = intent {
        val tokens = getTokenUseCase()
        val user = getUserStoreUseCase()

        val tempStartDate =
            dataSource.getStartOfMonth(LocalDate.now().year, LocalDate.now().monthValue)
        val tempEnddate = dataSource.getEndOfMonth(LocalDate.now().year, LocalDate.now().monthValue)

        val result = getWeeklyWorkUseCase(
            accessToken = "Bearer ${tokens.first}",
            calendarGroupId = user?.workGroupId ?: 0,
            startDate = tempStartDate.toString(),
            endDate = tempEnddate.toString(),
        ).getOrNull()

        val userName = getUserStoreUseCase()!!.userName
        val scheduleWork = formatWork(result, userName)

        reduce {
            state.copy(myWorkList = scheduleWork)
        }
    }


    // 선택한 유저의 근무일정 조회
    suspend fun getGroupWork(userId: Long, userName: String): Map<String, Map<String, String>> {
        val tokens = getTokenUseCase()

        val tempStartDate =
            dataSource.getStartOfMonth(LocalDate.now().year, LocalDate.now().monthValue)
        val tempEnddate = dataSource.getEndOfMonth(LocalDate.now().year, LocalDate.now().monthValue)

        val result = getMembersWorkUseCase(
            accessToken = "Bearer ${tokens.first}",
            userGroupId = groupId,
            targetUserId = userId,
            startDate = tempStartDate.toString(),
            endDate = tempEnddate.toString(),
        ).getOrNull()

        val scheduleWork = formatGroupMembersWork(result!!, userName)
        return scheduleWork
    }


    // 그룹 정보 조회
    private fun getGroupInfo(groupId: Long) = intent {
        val tokens = getTokenUseCase()
        val result = groupInfoUseCase(
            accessToken = "Bearer ${tokens.first}",
            groupId = groupId
        ).getOrThrow()

        reduce {
            state.copy(
                groupInfo = result,
                tempGroupName = result.groupName
            )
        }
    }

    // 주간 달력 터치 & 함께 일하는 동료 조회
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
                endDate = finalStartDate,
                selectedMembers = emptyList(),
                groupWorkList = emptyMap()
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
                ),
                selectedMembers = emptyList(),
                groupWorkList = emptyMap()
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
                endDate = finalStartDate.plusDays(7),
                selectedMembers = emptyList(),
                groupWorkList = emptyMap()
            )
        }
        getGroupDuty()
    }

    fun getGroupDuty() = intent {
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
    }


    // 초대 링크 생성 및 복사
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

        Log.d("invitationCode", "${state.invitationCode}")
    }

    fun copyInvitationLink(context: Context) = intent {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        getInvitationCode()

        // 수정 예정
        val clip = ClipData.newPlainText("label", state.invitationCode)
        clipboard.setPrimaryClip(clip)
    }


    // 그룹 수정
    fun updateGroupInfo() = intent {
        val tokens = getTokenUseCase()

        val newPassword = if (state.tempGroupPassword.length == 6) {
            state.tempGroupPassword
        } else {
            ""
        }
        val result = updateGroupInfoUseCase(
            accessToken = "Bearer ${tokens.first}",
            groupId = groupId,
            newName = state.tempGroupName,
            newPassword = newPassword
        )

        if (result.isSuccess) {
            reduce {
                state.copy(
                    groupInfo = state.groupInfo.copy(
                        groupName = state.tempGroupName
                    ),
                    tempGroupPassword = ""
                )
            }
            postSideEffect(GroupDetailSideEffect.Toast("수정 완료되었습니다."))
        } else {
            Log.d("updateGroupInfo", "${result}")
            postSideEffect(GroupDetailSideEffect.Toast("그룹 정보 수정이 실패하였습니다."))
        }
    }

    fun onClickCancel() = intent {
        reduce {
            state.copy(
                tempGroupName = state.groupInfo.groupName,
                tempGroupPassword = ""
            )
        }
    }

    @OptIn(OrbitExperimental::class)
    fun onChangeGroupName(value: String) = blockingIntent {
        reduce {
            state.copy(
                tempGroupName = value
            )
        }
    }


    @OptIn(OrbitExperimental::class)
    fun onChangeGroupPassword(value: String) = blockingIntent {
        reduce {
            state.copy(
                tempGroupPassword = value
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
        groupName = "임시",
        admin = Admin(
            userId = null,
            groupId = 12,
            userName = "도",
            profileImgUrl = "https://ssl.pstatic.net/static/pwe/address/img_profile.png",
            isAdmin = true
        ),
        groupMembers = emptyList()
    ),

    val tempGroupName: String = "",
    val tempGroupPassword: String = "",

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

sealed interface GroupDetailSideEffect {
    class Toast(val message: String) : GroupDetailSideEffect
}