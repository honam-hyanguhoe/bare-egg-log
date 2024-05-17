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
import com.org.egglog.domain.group.model.GroupMember
import com.org.egglog.domain.group.model.Member
import com.org.egglog.domain.group.usecase.ChangeLeaderUseCase
import com.org.egglog.domain.group.usecase.DeleteMemberUseCase
import com.org.egglog.domain.group.usecase.ExitGroupUseCase
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
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Calendar
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
    private val deleteMemberUseCase: DeleteMemberUseCase,
    private val changeLeaderUseCase: ChangeLeaderUseCase,
    private val exitGroupUseCase: ExitGroupUseCase,
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

    private val _showUploadBottomSheet = MutableStateFlow(false)
    val showUploadBottomSheet : StateFlow<Boolean> = _showUploadBottomSheet.asStateFlow()
    private val _showSecondUploadBottomSheet = MutableStateFlow(false)
    val showSecondUploadBottomSheet : StateFlow<Boolean> = _showSecondUploadBottomSheet.asStateFlow()
    private val _showDateBottomSheet = MutableStateFlow(false)
    val showDateBottomSheet : StateFlow<Boolean> = _showDateBottomSheet.asStateFlow()
    private val _selectedDate = MutableStateFlow<LocalDate?>(null)
    val selectedDate : StateFlow<LocalDate?> = _selectedDate.asStateFlow()

    init {
        loadInit()
    }

    private fun loadInit() = intent {
        getGroupInfo(groupId = groupId)
        Log.d("weekCal", "init ${state.startDate} ${state.currentWeekDays}")
    }

    fun initSelectedMembers() = intent {
        reduce {
            state.copy(
                selectedMembers = emptyList(),
                groupWorkList = emptyMap(),
            )
        }
    }

    fun setSelectedWorkDate(selected: LocalDate?) = intent{
        _selectedDate.value = selected
        Log.d("group", "selected $selected")

        reduce {
            state.copy(
                selectedWorkDate = selected,

            )
        }
    }

    fun setShowBottomSheet(show: Boolean) {
        _showBottomSheet.value = show
    }

    fun setShowDateBottomSheet(show: Boolean) {
        _showDateBottomSheet.value = show
    }
    fun setSelected(newValue: String) = intent {
        reduce { state.copy(dutyType = newValue) }
        updateData()
    }

    fun onClickUpload(show : Boolean){
        _showUploadBottomSheet.value = show
    }

    fun onClickNextUpload(show : Boolean){
        _showSecondUploadBottomSheet.value = show
    }

    fun getSelectedDateWork() = intent {

        Log.d("date group", "선택한 날짜 ${state.selectedWorkDate}")
        getMyWork(state.selectedWorkDate)

        Log.d("date group", "선택한 날짜 - 내 근무 ${state.myWorkList}")
        val currentMembers = state.selectedMembers
        var newGroupWorkList : Map<String, Map<String, String>> = emptyMap()

        currentMembers.forEach { member ->
            val memberWork = getGroupWork(member.userId, member.userName, state.selectedWorkDate)
            Log.d("date group", "memberWork ${memberWork}")
            newGroupWorkList = newGroupWorkList +  memberWork
        }

        reduce {
            state.copy(
                groupWorkList = newGroupWorkList
            )
        }
        Log.d("date group", "get ${state.groupWorkList}")
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
                    currentGroupWorkList + getGroupWork(member.userId, member.userName, state.selectedWorkDate)
            } else {
                newMembers = currentMembers
                newGroupWorkList = currentGroupWorkList
            }
            reduce {
                state.copy(selectedMembers = newMembers, groupWorkList = newGroupWorkList)
            }

            Log.d("date group", "gwl ${state.groupWorkList}")
        }
    }
    
    // 근무 값 보정
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

    suspend fun getMyWork(date : LocalDate?) = intent {
        val tokens = getTokenUseCase()
        val user = getUserStoreUseCase()
        
        val tempStartAndEndDate = getStartAndEndOfMonth(date!!.year, date.monthValue )
        val tempStartDate = tempStartAndEndDate.first
        val tempEnddate = tempStartAndEndDate.second
        Log.d("date group", "mymymym $tempStartDate -- $tempEnddate")


        val result = getWeeklyWorkUseCase(
            accessToken = "Bearer ${tokens.first}",
            calendarGroupId = user?.workGroupId ?: 0,
            startDate = tempStartDate.toString(),
            endDate = tempEnddate.toString(),
        ).getOrNull()

        val userName = getUserStoreUseCase()!!.userName
        val scheduleWork = formatWork(result, userName)
        Log.d("date group", "sw ${scheduleWork}")
        reduce {
            state.copy(myWorkList = scheduleWork)
        }
    }

    // 년, 월이 주어졌을 때 시작일과 마지막 일 구하기
    fun getStartAndEndOfMonth(year: Int, month: Int): Pair<String, String> {
        val yearMonth = YearMonth.of(year, month)
        val startDate = yearMonth.atDay(1)
        val endDate = yearMonth.atEndOfMonth()

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formattedStartDate = startDate.format(formatter)
        val formattedEndDate = endDate.format(formatter)

        return Pair(formattedStartDate, formattedEndDate)
    }

    // 선택한 유저의 근무일정 조회
    suspend fun getGroupWork(userId: Long, userName: String, date : LocalDate?): Map<String, Map<String, String>>  {
        val tokens = getTokenUseCase()

        val tempStartAndEndDate = getStartAndEndOfMonth(date!!.year, date.monthValue )
        val tempStartDate = tempStartAndEndDate.first
        val tempEnddate = tempStartAndEndDate.second
        Log.d("date group", "$tempStartDate -- $tempEnddate")

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

        Log.d("groupInfo", "result $result")
        reduce {
            state.copy(
                groupInfo = result,
                tempGroupName = result.groupName
            )
        }
    }

    // 주간 달력 터치 & 함께 일하는 동료 조회
    fun onPrevClick(clickedDate: LocalDate) = intent {
        val finalStartDate = dataSource.getStartOfWeek(clickedDate)
        val calendarUiModel = dataSource.getData(startDate = finalStartDate, lastSelectedDate = finalStartDate)

        reduce {
            state.copy(
                currentWeekDays = calendarUiModel,
                startDate = calendarUiModel.startDate.date,
                endDate = finalStartDate,
                selectedMembers = emptyList(),
                groupWorkList = emptyMap()
            )
        }
        Log.d("weekCal", "${state.startDate} ${state.endDate}")
        getGroupDuty()
        val userName = getUserStoreUseCase()!!.userName
    }

    fun capitalizeFirstLetter(input: String): String {
        return input.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
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
        Log.d("weekCal", "${state.currentWeekDays}")
        getGroupDuty()
    }

    fun onNextClick(localDate: LocalDate) = intent {
        val finalStartDate = dataSource.getEndOfWeek(localDate).plusDays(2)
//        val calendarUiModel: WeeklyUiModel =
//            dataSource.getData(
//                startDate = finalStartDate,
//                lastSelectedDate = finalStartDate.plusDays(1)
//            )
        val   calendarUiModel = dataSource.getData(startDate = finalStartDate, lastSelectedDate = finalStartDate.minusDays(1))
//        Log.d("weekCal", "cum $calendarUiModel")

        reduce {
            state.copy(
                currentWeekDays = calendarUiModel,
                startDate = calendarUiModel.startDate.date,
                endDate = finalStartDate.plusDays(5),
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
        Log.d("weekCal", "result $result")
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
    private suspend fun getInvitationCode() : String{
        val tokens = getTokenUseCase()
        val result = getInvitationCodeUseCase(
            accessToken = "Bearer ${tokens.first}",
            groupId = groupId
        ).getOrNull()

        return result ?: ""
    }

    fun copyInvitationLink(context: Context) = intent {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val invitationCode = getInvitationCode()

        Log.d("Invite Link", invitationCode)
        val invitationLink = "egglog://honam.com/invite/${invitationCode}/${state.groupInfo.groupName}"
        Log.d("Invite Link", "link $invitationLink")
        // 수정 예정
        val clip = ClipData.newPlainText("Invite Link", invitationLink)
        clipboard.setPrimaryClip(clip)
        postSideEffect(GroupDetailSideEffect.Toast("초대링크가 복사되었습니다"))
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


    // 그룹원 관리
    fun onDelete(userId : Long) = intent {
        val tokens = getTokenUseCase()
        val result = deleteMemberUseCase(
            accessToken = "Bearer ${tokens.first}",
            groupId = groupId,
            memberId = userId
        )
        if(result.isSuccess){
            reduce {
                state.copy(
                    groupInfo = state.groupInfo.copy(
                        groupMembers = state.groupInfo.groupMembers?.filter { it.userId != userId }
                    )
                )
            }
            Log.d("memberManageScreen", "groupMembers ${state.groupInfo.groupMembers}")
            postSideEffect(GroupDetailSideEffect.Toast("멤버를 삭제하였습니다."))
        }else{
            postSideEffect(GroupDetailSideEffect.Toast("멤버 삭제에 실패하였습니다."))
        }
    }


    fun onChangeLeader(member : GroupMember) = intent {
        val tokens = getTokenUseCase()
        val result = changeLeaderUseCase(
            accessToken = "Bearer ${tokens.first}",
            groupId = groupId,
            memberId = member.userId!!
        )

        Log.d("memberManageScreen", "result $result")

        if(result.isSuccess){
            reduce {
                state.copy(
                    groupInfo = state.groupInfo.copy(
                        isAdmin = false,
                        admin = Admin(
                            userId = member.userId,
                            groupId = groupId,
                            userName = member.userName,
                            profileImgUrl = member.profileImgUrl,
                            isAdmin = true
                        )
                    )
                )
            }
            Log.d("memberManageScreen", "groupMembers ${state.groupInfo}")
            postSideEffect(GroupDetailSideEffect.Toast("모임장이 변경되었습니다."))
        }else{
            postSideEffect(GroupDetailSideEffect.Toast("모임장 위임에 실패하였습니다."))
        }
    }

    fun exitGroup() = intent {
        val tokens = getTokenUseCase()
        val result = exitGroupUseCase(
            accessToken = "Bearer ${tokens.first}",
            groupId = groupId
        )

        if(result.isSuccess){
            postSideEffect(GroupDetailSideEffect.Toast("그룹을 탈퇴하였습니다."))
        }else{
            postSideEffect(GroupDetailSideEffect.Toast("다시 시도해주세요"))
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
            isAdmin = true,
        ),
        groupMembers = emptyList()
    ),

    val tempGroupName: String = "",
    val tempGroupPassword: String = "",

    // 주간 달력 클릭
    val currentWeekDays: WeeklyUiModel = WeeklyDataSource().getData(
        LocalDate.now(),
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



    // 선택한 멤버 근무 조회
    val selectedWorkDate : LocalDate? = LocalDate.now(),
    val selectedMembers: List<Member> = emptyList(),
    val myWorkList: Map<String, Map<String, String>> = emptyMap(),
    val groupWorkList: Map<String, Map<String, String>> = emptyMap()
)

sealed interface GroupDetailSideEffect {
    class Toast(val message: String) : GroupDetailSideEffect
}