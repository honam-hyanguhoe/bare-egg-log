package com.org.egglog.presentation.domain.myCalendar.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.org.egglog.domain.auth.model.UserDetail
import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.domain.auth.usecase.GetUserStoreUseCase
import com.org.egglog.domain.myCalendar.model.AddWorkData
import com.org.egglog.domain.myCalendar.model.EditWorkData
import com.org.egglog.domain.myCalendar.model.EventListData
import com.org.egglog.domain.myCalendar.model.PersonalScheduleData
import com.org.egglog.domain.myCalendar.model.WorkListData
import com.org.egglog.domain.myCalendar.model.WorkScheduleData
import com.org.egglog.domain.myCalendar.model.WorkType
import com.org.egglog.domain.myCalendar.usecase.CreatePersonalScheduleUseCase
import com.org.egglog.domain.myCalendar.usecase.CreateWorkScheduleUseCase
import com.org.egglog.domain.myCalendar.usecase.DeletePersonalScheduleUseCase
import com.org.egglog.domain.myCalendar.usecase.EditWorkScheduleUseCase
import com.org.egglog.domain.myCalendar.usecase.GetDetailPersonalScheduleUseCase
import com.org.egglog.domain.myCalendar.usecase.GetPersonalScheduleUseCase
import com.org.egglog.domain.myCalendar.usecase.GetWorkListUseCase
import com.org.egglog.domain.myCalendar.usecase.GetWorkTypeListUseCase
import com.org.egglog.domain.myCalendar.usecase.ModifyPersonalScheduleUseCase
import com.org.egglog.domain.scheduler.usecase.GetAlarmFindListUseCase
import com.org.egglog.domain.scheduler.usecase.NotificationUseCase
import com.org.egglog.domain.scheduler.usecase.SchedulerUseCase
import com.org.egglog.domain.setting.model.CalendarGroup
import com.org.egglog.domain.setting.usecase.GetCalendarGroupListUseCase
import com.org.egglog.domain.setting.usecase.GetCalendarGroupMapStoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
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
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class MyCalendarViewModel @Inject constructor(
    private val getTokenUseCase: GetTokenUseCase,
    private val getWorkTypeListUseCase: GetWorkTypeListUseCase,
    private val createPersonalScheduleUseCase: CreatePersonalScheduleUseCase,
    private val getUserStoreUseCase: GetUserStoreUseCase,
    private val createWorkScheduleUseCase: CreateWorkScheduleUseCase,
    private val editWorkScheduleUseCase: EditWorkScheduleUseCase,
    private val getWorkListUseCase: GetWorkListUseCase,
    private val getPersonalScheduleUseCase: GetPersonalScheduleUseCase,
    private val deletePersonalScheduleUseCase: DeletePersonalScheduleUseCase,
    private val getDetailPersonalScheduleUseCase: GetDetailPersonalScheduleUseCase,
    private val modifyPersonalScheduleUseCase: ModifyPersonalScheduleUseCase,
    private val getCalendarGroupMapStoreUseCase: GetCalendarGroupMapStoreUseCase,
    private val getAlarmFindListUseCase: GetAlarmFindListUseCase,
    private val notificationUseCase: NotificationUseCase,
    private val schedulerUseCase: SchedulerUseCase,
    private val getCalendarGroupListUseCase: GetCalendarGroupListUseCase
) : ViewModel(), ContainerHost<MyCalenderState, MyCalendarSideEffect> {

    private var accessToken: String? = null
    private var userInfo: UserDetail? = null

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    override val container: Container<MyCalenderState, MyCalendarSideEffect> = container(
        initialState = MyCalenderState(),
        buildSettings = {
            this.exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                intent { postSideEffect(MyCalendarSideEffect.Toast(throwable.message.orEmpty())) }
            }
        }
    )

    init {
        load()
    }

    private fun load() = intent {
        accessToken = getTokenUseCase().first!!

        Log.e("MyCalendarViewModel", "토큰 ${accessToken}")

        getWorkTypeList()

        userInfo = getUserStoreUseCase()

        Log.e("MyCalendarViewModel", "제 그룹 아이디는 ${userInfo!!.workGroupId}")

        getWorkList()
        getPersonalList()

    }

    fun refreshSomething() = intent {
        _isLoading.value = true
        delay(1000L)
        _isLoading.value = false

        getWorkList()
        getPersonalList()
    }

    fun getWorkTypeList() = intent {
        val workTypeList = getWorkTypeListUseCase("Bearer $accessToken").getOrThrow()

        reduce {
            state.copy(
                workTypeList = workTypeList.plus(
                    WorkType(
                        0,
                        "NONE",
                        "#D0D5DD",
                        "",
                        "NONE",
                        "",
                        ""
                    )
                )
            )
        }
    }

    private fun getWorkList() = intent {
        reduce {
            state.copy(monthlyWorkList = listOf())
        }

        val calendar = Calendar.getInstance()

        calendar.set(Calendar.YEAR, state.currentYear)
        calendar.set(Calendar.MONTH, state.currentMonth - 1)
        val lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        val month =
            if (state.currentMonth < 10) "0${state.currentMonth}" else "${state.currentMonth}"

        val startDate = "${state.currentYear}-${month}-01"
        val endDate = "${state.currentYear}-${month}-${lastDayOfMonth}"

        val monthlyWorkList =
            getWorkListUseCase("Bearer $accessToken", startDate, endDate).getOrThrow()

        reduce {
            state.copy(
                monthlyWorkList = monthlyWorkList.workList
            )
        }

        updateCurrentWorkData()
    }

    private fun getPersonalList() = intent {
        reduce {
            state.copy(monthlyPersonalList = listOf())
        }

        val calendarGroupMap = getCalendarGroupMapStoreUseCase()

        val calendar = Calendar.getInstance()

        calendar.set(Calendar.YEAR, state.currentYear)
        calendar.set(Calendar.MONTH, state.currentMonth - 1)
        val lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        val month =
            if (state.currentMonth < 10) "0${state.currentMonth}" else "${state.currentMonth}"

        val startDate = "${state.currentYear}-${month}-01"
        val endDate = "${state.currentYear}-${month}-${lastDayOfMonth}"


        for ((calendarGroupId, isActive) in calendarGroupMap.entries) {
            if (isActive) {
                val monthlyPersonalList = getPersonalScheduleUseCase(
                    "Bearer $accessToken",
                    startDate,
                    endDate,
                    userInfo!!.id,
                    calendarGroupId.toLong()
                ).getOrThrow()

                Log.e("myCalendar", calendarGroupId)

                val updateMonthlyPersonalList = monthlyPersonalList.map { personalScheduleData ->
                    val updatedEventList = personalScheduleData.eventList.map { event ->
                        if (event.calendarGroupId != userInfo!!.workGroupId) {
                            event.copy(eventContent = "[구독 캘린더] ${event.eventContent ?: "내용 없음"}") // Modify eventContent as needed
                        } else {
                            event
                        }
                    }
                    personalScheduleData.copy(eventList = updatedEventList)
                }

                reduce {
                    state.copy(
                        monthlyPersonalList = state.monthlyPersonalList.plus(
                            updateMonthlyPersonalList
                        )
                    )
                }
            }
        }
        updateCurrentEventData()
    }

    fun onSelectedIdx(selectedIdx: Int) = intent {
        when (selectedIdx) {
            1 -> postSideEffect(MyCalendarSideEffect.NavigateToGroupActivity) // 그룹 페이지로 이동
            2 -> postSideEffect(MyCalendarSideEffect.NavigateToMainActivity) // 메인 페이지로 이동
            3 -> postSideEffect(MyCalendarSideEffect.NavigateToCommunityActivity)
            4 -> postSideEffect(MyCalendarSideEffect.NavigateToSettingActivity) // 설정 페이지로 이동
        }
    }

    @OptIn(OrbitExperimental::class)
    fun onChangeScheduleTitle(title: String) = blockingIntent {
        reduce {
            state.copy(scheduleTitle = title)
        }
    }

    @OptIn(OrbitExperimental::class)
    fun onChangeScheduleContent(content: String) = blockingIntent {
        reduce {
            state.copy(scheduleContent = content)
        }
    }

    fun onChangeStartTime(startTime: LocalDateTime) = intent {
        reduce {
            state.copy(startTime = startTime)
        }
    }

    fun onChangeEndTime(endTime: LocalDateTime) = intent {
        reduce {
            state.copy(endTime = endTime)
        }
    }

    private fun stringToLocalDateTime(dateTimeString: String): LocalDateTime {
        return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }


    fun onSubmitPersonalSchedule() = intent {
        val response = createPersonalScheduleUseCase(
            "Bearer $accessToken",
            state.scheduleTitle,
            state.scheduleContent,
            state.startTime,
            state.endTime,
            userInfo!!.workGroupId!!
        )
        if (response.isSuccess) {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val list = getPersonalScheduleUseCase(
                "Bearer $accessToken",
                LocalDate.now().plusDays(0).format(formatter),
                LocalDate.now().plusDays(1).format(formatter),
                userInfo!!.id,
                userInfo!!.workGroupId!!
            ).getOrThrow()
            list.map { dateList ->
                dateList.eventList.map { notificationUseCase.setNotification(it.eventId, stringToLocalDateTime(it.startDate).minusMinutes(30L)) }
            }
            postSideEffect(MyCalendarSideEffect.Toast("등록되었습니다!"))
        } else {
            postSideEffect(MyCalendarSideEffect.Toast("등록에 실패했습니다\n다시 시도해주세요"))
        }

        getPersonalList()

        reduce {
            state.copy(
                scheduleTitle = "",
                scheduleContent = "",
                startTime = LocalDateTime.now(),
                endTime = LocalDateTime.now()
            )
        }
    }

    fun onClickCalendarSetting() = intent {
        postSideEffect(MyCalendarSideEffect.NavigateToCalendarSettingScreen)
    }

    fun onPrevMonthClick() = intent {
        reduce {
            state.copy(
                monthlyWorkList = listOf()
            )
        }

        getWorkList()
        getPersonalList()

        if (state.currentMonth == 1) {
            reduce {
                state.copy(
                    currentMonth = 12,
                    currentYear = state.currentYear - 1
                )
            }
        } else {
            reduce {
                state.copy(currentMonth = state.currentMonth - 1)
            }
        }
    }

    fun onNextMonthClick() = intent {
        reduce {
            state.copy(
                monthlyWorkList = listOf()
            )
        }

        getWorkList()
        getPersonalList()

        if (state.currentMonth == 12) {
            reduce {
                state.copy(
                    currentMonth = 1,
                    currentYear = state.currentYear + 1
                )
            }
        } else {
            reduce {
                state.copy(currentMonth = state.currentMonth + 1)
            }
        }
    }

    private fun updateCurrentWorkData() = intent {
        val date =
            if (state.selectedDate < 10) "0${state.selectedDate}" else "${state.selectedDate}"

        val currentWorkData =
            state.monthlyWorkList.find { it.workDate.substring(8, 10) == date }?.workType

        reduce {
            state.copy(
                currentWorkData = currentWorkData
            )
        }
    }

    private fun updateCurrentEventData() = intent {
        val date =
            if (state.selectedDate < 10) "0${state.selectedDate}" else "${state.selectedDate}"

        val currentPersonalData =
            state.monthlyPersonalList.filter { it.date.substring(8, 10) == date }
                .flatMap { it.eventList }

        reduce {
            state.copy(
                currentPersonalData = currentPersonalData
            )
        }
    }

    fun onDateClicked(clickedDate: Int) = intent {
        reduce {
            state.copy(
                selectedDate = clickedDate
            )
        }

        updateCurrentWorkData()
        updateCurrentEventData()
    }

    fun onWorkLabelClick(workType: WorkType) = intent {
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.YEAR, state.currentYear)
        calendar.set(Calendar.MONTH, state.currentMonth - 1)
        val lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        val month =
            if (state.currentMonth < 10) "0${state.currentMonth}" else "${state.currentMonth}"
        val date =
            if (state.selectedDate < 10) "0${state.selectedDate}" else "${state.selectedDate}"
        val workDate = "${state.currentYear}-${month}-${date}"


        // 기존 근무 일정에 있는지 없는지 판별
        val targetWorkId: Long? = state.monthlyWorkList // 모든 WorkListInfo를 하나의 리스트로 평면화
            .find { it.workDate == workDate } // 원하는 날짜와 같은 workDate를 가진 첫 번째 WorkListInfo 반환
            ?.workId

        if (targetWorkId == null) { // 생성 요청 보내기
            val index = state.createWorkList.indexOfFirst { it.workDate == workDate }
            if (workType.title != "NONE") { // 근무 타입 라벨 눌렀을 때
                if (index == -1) {
                    // 같은 요소가 없음
                    reduce {
                        state.copy(
                            createWorkList = state.createWorkList.plus(
                                AddWorkData(
                                    workDate,
                                    workType.workTypeId
                                )
                            )
                        )
                    }
                } else {
                    // 같은 요소가 있음
                    val updateWorkList = state.createWorkList.toMutableList()
                    updateWorkList[index] =
                        updateWorkList[index].copy(workTypeId = workType.workTypeId)
                    reduce {
                        state.copy(
                            createWorkList = updateWorkList
                        )
                    }
                }
            } else { // 'None' 라벨 눌렀을 때
                if (index == -1) {
                    // 같은 요소가 없음

                } else {
                    // 같은 요소가 있음
                    val updateWorkList = state.createWorkList.toMutableList()
                    updateWorkList.removeAt(index)
                    reduce {
                        state.copy(
                            createWorkList = updateWorkList
                        )
                    }
                }
            }
        } else {  // 수정, 삭제 요청 보내기
            val index =
                state.editWorkList.indexOfFirst { it.workDate == workDate } // editList에 workDate가 같은 index
            if (workType.title != "NONE") { // 근무 타입 라벨 클릭시 => 수정 요청
                if (index == -1) { // index가 없으면 새로운 수정 목록 추가
                    reduce {
                        state.copy(
                            editWorkList = state.editWorkList.plus(
                                EditWorkData(
                                    targetWorkId,
                                    workDate,
                                    workType.workTypeId,
                                    false
                                )
                            )
                        )
                    }
                } else {
                    val updateWorkList = state.editWorkList.toMutableList()
                    updateWorkList[index] = updateWorkList[index].copy(
                        workTypeId = workType.workTypeId,
                        isDeleted = false
                    )
                    reduce {
                        state.copy(
                            editWorkList = updateWorkList
                        )
                    }
                }
            } else { // None 라벨 클릭시 => 삭제 요청
                if (index == -1) {
                    reduce {
                        state.copy(
                            editWorkList = state.editWorkList.plus(
                                EditWorkData(
                                    targetWorkId,
                                    workDate,
                                    workType.workTypeId,
                                    true
                                )
                            )
                        )
                    }

                } else {
                    val updateWorkList = state.editWorkList.toMutableList()
                    updateWorkList[index] = updateWorkList[index].copy(
                        isDeleted = true
                    )
                }
            }
        }


        // 날짜 이동 로직 start
        if (workType.title != "NONE") {
            if (state.selectedDate <= lastDayOfMonth) {
                val index =
                    state.tempWorkList.indexOfFirst { it.first == state.selectedDate } // 이미 입력된 tempWork 가 있따면
                if (index == -1) {
                    reduce {
                        state.copy(
                            tempWorkList = state.tempWorkList.plus(
                                Pair(
                                    state.selectedDate,
                                    workType
                                )
                            )
                        )
                    }
                } else {
                    val updateWorkList = state.tempWorkList.toMutableList()
                    updateWorkList[index] = updateWorkList[index].copy(second = workType)
                    reduce {
                        state.copy(
                            tempWorkList = updateWorkList
                        )
                    }
                }
                reduce {
                    state.copy(
                        selectedDate = if (state.selectedDate == lastDayOfMonth) state.selectedDate else state.selectedDate + 1,
                    )
                }
            } else {
                postSideEffect(MyCalendarSideEffect.Toast("더이상 추가할 수 없습니다"))
            }
        } else {
            val index =
                state.tempWorkList.indexOfFirst { it.first == state.selectedDate } // 이미 입력된 tempWork 가 있따면
            if (state.selectedDate > 0) {
                if (index == -1) {
                    reduce {
                        state.copy(
                            tempWorkList = state.tempWorkList.plus(
                                Pair(
                                    state.selectedDate,
                                    workType
                                )
                            )
                        )
                    }
                } else {
                    val updateWorkList = state.tempWorkList.toMutableList()
                    updateWorkList[index] = updateWorkList[index].copy(second = workType)
                    reduce {
                        state.copy(
                            tempWorkList = updateWorkList
                        )
                    }
                }
                reduce {
                    state.copy(
                        selectedDate = if (state.selectedDate == 1) state.selectedDate else state.selectedDate - 1,
                    )
                }
            } else {
                postSideEffect(MyCalendarSideEffect.Toast("더이상 삭제할 수 없습니다"))
            }
        }
        // 날짜 이동 로직 end
        updateCurrentWorkData()
        updateCurrentEventData()

    }

    private fun stringToNewLocalDateTime(dateTimeString: String): LocalDateTime {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm")
        return LocalDateTime.parse(dateTimeString, formatter)
    }

    fun onSubmitWorkSchedule() = intent {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        if (state.editWorkList.isNotEmpty()) {
            val response = editWorkScheduleUseCase(
                "Bearer $accessToken",
                userInfo!!.workGroupId!!,
                state.editWorkList
            )
            if (response.isSuccess) {
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
                postSideEffect(MyCalendarSideEffect.Toast("등록되었습니다!"))
            } else {
                postSideEffect(MyCalendarSideEffect.Toast("등록에 실패하였습니다. 다시 시도해주세요."))
            }
        }
        if (state.createWorkList.isNotEmpty()) {
            val response = createWorkScheduleUseCase(
                "Bearer $accessToken",
                userInfo!!.workGroupId!!,
                state.createWorkList
            )
            if (response.isSuccess) {
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
                postSideEffect(MyCalendarSideEffect.Toast("등록되었습니다!"))
            } else {
                postSideEffect(MyCalendarSideEffect.Toast("등록에 실패하였습니다. 다시 시도해주세요."))
            }
        }

        getWorkList()

        reduce {
            state.copy(
                createWorkList = listOf(),
                editWorkList = listOf(),
                tempWorkList = listOf()
            )
        }
    }

    fun onCancelWorkSchedule() = intent {
        reduce {
            state.copy(
                tempWorkList = listOf(),
                createWorkList = listOf(),
                editWorkList = listOf(),
            )
        }
    }

    fun onDeletePersonalSchedule(eventId: Int) = intent {
        val response = deletePersonalScheduleUseCase("Bearer $accessToken", eventId)

        if (response.isSuccess) {
            notificationUseCase.cancelAlarm(eventId)
            postSideEffect(MyCalendarSideEffect.Toast("삭제되었습니다"))
            getPersonalList()
        } else {
            postSideEffect(MyCalendarSideEffect.Toast("삭제 실패. 다시 시도해주세요."))
        }
    }

    fun onClickModify(eventId: Int) = intent {
        // eventId의 일정 정보 불러오기
        val eventData =
            getDetailPersonalScheduleUseCase("Bearer $accessToken", eventId).getOrThrow()

        reduce {
            state.copy(
                scheduleTitle = eventData.eventTitle,
                scheduleContent = eventData.eventContent ?: "",
                isModifyState = true,
                currentEventId = eventData.eventId
            )
        }
    }

    fun onModifyPersonalSchedule(eventId: Int) = intent {
        // eventId의 일정 수정 하기
        val response = modifyPersonalScheduleUseCase(
            "Bearer $accessToken",
            eventId,
            state.scheduleTitle,
            state.scheduleContent,
            state.startTime,
            state.endTime,
            userInfo!!.workGroupId!!
        )

        if (response.isSuccess) {
            // 오늘 혹은 내일 날짜
            if(state.startTime.minusMinutes(30L) > LocalDateTime.now() && state.startTime.dayOfMonth < LocalDateTime.now().dayOfMonth + 2) notificationUseCase.setNotification(eventId, state.startTime.minusMinutes(30L))
            postSideEffect(MyCalendarSideEffect.Toast("수정 되었습니다!"))
            getPersonalList()
        } else {
            postSideEffect(MyCalendarSideEffect.Toast("수정 되지 않았습니다. 다시 시도해주세요."))
        }

        reduce {
            state.copy(
                scheduleTitle = "",
                scheduleContent = "",
                startTime = LocalDateTime.now(),
                endTime = LocalDateTime.now(),
                isModifyState = false,
                currentEventId = -1
            )
        }
    }
}

data class MyCalenderState(
    val selectedIdx: Int = 0,
    val scheduleTitle: String = "",
    val scheduleContent: String = "",
    val startTime: LocalDateTime = LocalDateTime.now(),
    val endTime: LocalDateTime = LocalDateTime.now(),
    val currentYear: Int = Calendar.getInstance().get(Calendar.YEAR),
    val currentMonth: Int = Calendar.getInstance().get(Calendar.MONTH) + 1,
    val selectedDate: Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
    val workTypeList: List<WorkType> = listOf(),
    val monthlyWorkList: List<WorkListData> = listOf(), // 서버로부터 조회한 월간 근무 일정
    val createWorkList: List<AddWorkData> = listOf(),
    val editWorkList: List<EditWorkData> = listOf(),
    val tempWorkList: List<Pair<Int, WorkType?>> = listOf(), // 근무 입력시 달력에 표시할 내용 (workDate, workTitle)
    val currentWorkData: WorkType? = null,
    val monthlyPersonalList: List<PersonalScheduleData> = listOf(),
    val currentPersonalData: List<EventListData>? = listOf(),
    val isModifyState: Boolean = false,
    val currentEventId: Int = -1,
    val calendarGroupList: List<CalendarGroup> = listOf()
)

sealed interface MyCalendarSideEffect {
    class Toast(val message: String) : MyCalendarSideEffect

    data object NavigateToMainActivity : MyCalendarSideEffect

    data object NavigateToGroupActivity : MyCalendarSideEffect

    data object NavigateToSettingActivity : MyCalendarSideEffect

    data object NavigateToCommunityActivity : MyCalendarSideEffect

    data object NavigateToCalendarSettingScreen : MyCalendarSideEffect
}