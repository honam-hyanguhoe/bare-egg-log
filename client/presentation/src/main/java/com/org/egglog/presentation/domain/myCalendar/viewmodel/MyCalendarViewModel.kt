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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import java.time.LocalDateTime
import java.time.LocalTime
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
    private val modifyPersonalScheduleUseCase: ModifyPersonalScheduleUseCase
) : ViewModel(), ContainerHost<MyCalenderState, MyCalendarSideEffect> {

    private var accessToken: String? = null
    private var userInfo: UserDetail? = null

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

        // TODO 이번달 1일~마지막일 근무, 개인 일정 리스트 불러오기
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
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.YEAR, state.currentYear)
        calendar.set(Calendar.MONTH, state.currentMonth - 1)
        val lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        val month =
            if (state.currentMonth < 10) "0${state.currentMonth}" else "${state.currentMonth}"

        val startDate = "${state.currentYear}-${month}-01"
        val endDate = "${state.currentYear}-${month}-${lastDayOfMonth}"

        val monthlyPersonalList = getPersonalScheduleUseCase(
            "Bearer $accessToken",
            startDate,
            endDate,
            userInfo!!.id,
            userInfo!!.workGroupId!!
        ).getOrThrow()
        reduce {
            state.copy(monthlyPersonalList = monthlyPersonalList)
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

    fun onSubmitPersonalSchedule() = intent {
        val response = createPersonalScheduleUseCase(
            "Bearer $accessToken",
            state.scheduleTitle,
            state.scheduleContent,
            state.startTime!!,
            state.endTime!!,
            userInfo!!.workGroupId!!
        )
        if (response.isSuccess) {
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
        // TODO 이전 달 일정 불러오기, 근무 일정 리스트에 대입
        reduce {
            state.copy(
                monthlyWorkList = listOf()
            )
        }

        getWorkList()

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
        // TODO 다음 달 일정 불러오기, 근무 일정 리스트에 대입
        reduce {
            state.copy(
                monthlyWorkList = listOf()
            )
        }

        getWorkList()

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
            state.monthlyPersonalList.find { it.date.substring(8, 10) == date }?.eventList

        reduce {
            state.copy(
                currentPersonalData = currentPersonalData
            )
        }
    }

    fun onDateClicked(clickedDate: Int) = intent {
        // TODO 해당 날짜 일정 리스트 불러오기
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
                                    null
                                )
                            )
                        )
                    }
                } else {
                    val updateWorkList = state.tempWorkList.toMutableList()
                    updateWorkList[index] = updateWorkList[index].copy(second = null)
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

    }

    fun onSubmitWorkSchedule() = intent {

        if (state.editWorkList.isNotEmpty()) {
            // TODO 근무 수정 요청 보내고 새로운 근무 일정 불러오기 (근무 조회 기능 구현 후 주석 풀 예정)
            val response = editWorkScheduleUseCase(
                "Bearer $accessToken",
                userInfo!!.workGroupId!!,
                state.editWorkList
            )
            if (response.isSuccess) {
                postSideEffect(MyCalendarSideEffect.Toast("등록되었습니다!"))
            } else {
                postSideEffect(MyCalendarSideEffect.Toast("등록에 실패하였습니다. 다시 시도해주세요."))
            }
        } else if (state.createWorkList.isNotEmpty()) {
            // TODO 근무 생성 요청 보내고 새로운 근무 일정 불러오기
            val response = createWorkScheduleUseCase(
                "Bearer $accessToken",
                userInfo!!.workGroupId!!,
                state.createWorkList
            )
            if (response.isSuccess) {
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
        Log.e("MyCalendarViewModel", "$eventId 번 일정 삭제")
        val response = deletePersonalScheduleUseCase("Bearer $accessToken", eventId)

        if (response.isSuccess) {
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
                scheduleContent = eventData.eventContent,
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

        if(response.isSuccess) {
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
    val currentEventId: Int = -1
)

sealed interface MyCalendarSideEffect {
    class Toast(val message: String) : MyCalendarSideEffect

    data object NavigateToMainActivity : MyCalendarSideEffect

    data object NavigateToGroupActivity : MyCalendarSideEffect

    data object NavigateToSettingActivity : MyCalendarSideEffect

    data object NavigateToCommunityActivity : MyCalendarSideEffect

    data object NavigateToCalendarSettingScreen : MyCalendarSideEffect
}