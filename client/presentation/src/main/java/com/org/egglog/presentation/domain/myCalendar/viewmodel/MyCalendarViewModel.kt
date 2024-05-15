package com.org.egglog.presentation.domain.myCalendar.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.org.egglog.domain.auth.model.UserDetail
import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.domain.auth.usecase.GetUserStoreUseCase
import com.org.egglog.domain.myCalendar.model.AddWorkData
import com.org.egglog.domain.myCalendar.model.EditWorkData
import com.org.egglog.domain.myCalendar.model.WorkListData
import com.org.egglog.domain.myCalendar.model.WorkScheduleData
import com.org.egglog.domain.myCalendar.model.WorkType
import com.org.egglog.domain.myCalendar.usecase.CreatePersonalScheduleUseCase
import com.org.egglog.domain.myCalendar.usecase.CreateWorkScheduleUseCase
import com.org.egglog.domain.myCalendar.usecase.EditWorkScheduleUseCase
import com.org.egglog.domain.myCalendar.usecase.GetWorkListUseCase
import com.org.egglog.domain.myCalendar.usecase.GetWorkTypeListUseCase
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
    private val getWorkListUseCase: GetWorkListUseCase
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

        val workTypeList = getWorkTypeListUseCase("Bearer $accessToken").getOrThrow()

        userInfo = getUserStoreUseCase()

        Log.e("MyCalendarViewModel", "제 그룹 아이디는 ${userInfo!!.workGroupId}")

        // TODO 이번달 1일~마지막일 근무, 개인 일정 리스트 불러오기
        getWorkList()

        reduce {
            state.copy(
                workTypeList = workTypeList.plus(
                    WorkType(
                        0,
                        "NONE",
                        "",
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

        val monthlyWorkList = getWorkListUseCase("Bearer $accessToken", startDate, endDate).getOrThrow()

        reduce {
            state.copy(
                monthlyWorkList = monthlyWorkList.workList
            )
        }
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

        reduce {
            state.copy(
                scheduleTitle = "",
                scheduleContent = "",
                startTime = null,
                endTime = null
            )
        }
    }

    fun onClickCalendarSetting() = intent {
        postSideEffect(MyCalendarSideEffect.NavigateToCalendarSettingScreen)
    }

    fun onPrevMonthClick() = intent {
        // TODO 이전 달 일정 불러오기, 근무 일정 리스트에 대입
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

        val currentWorkData = state.monthlyWorkList.find {it.workDate.substring(8,10) == date}?.workType

        reduce {
            state.copy (
                currentWorkData = currentWorkData
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
                val index = state.tempWorkList.indexOfFirst {it.first == state.selectedDate} // 이미 입력된 tempWork 가 있따면
                if(index == -1) {
                    reduce {
                        state.copy(
                            tempWorkList = state.tempWorkList.plus(
                                Pair(
                                    state.selectedDate,
                                    workType.title
                                )
                            )
                        )
                    }
                } else {
                    val updateWorkList = state.tempWorkList.toMutableList()
                    updateWorkList[index] = updateWorkList[index].copy(second = workType.title)
                    reduce {
                        state.copy (
                            tempWorkList = updateWorkList
                        )
                    }
                }
                reduce {
                    state.copy(
                        selectedDate = if(state.selectedDate == lastDayOfMonth) state.selectedDate else state.selectedDate + 1,
                    )
                }
            } else {
                postSideEffect(MyCalendarSideEffect.Toast("더이상 추가할 수 없습니다"))
            }
        } else {
            val index = state.tempWorkList.indexOfFirst {it.first == state.selectedDate} // 이미 입력된 tempWork 가 있따면
            if (state.selectedDate > 0) {
                if(index == -1) {
                    reduce {
                        state.copy(
                            tempWorkList = state.tempWorkList.plus(
                                Pair(
                                    state.selectedDate,
                                    ""
                                )
                            )
                        )
                    }
                } else {
                    val updateWorkList = state.tempWorkList.toMutableList()
                    updateWorkList[index] = updateWorkList[index].copy(second = "")
                    reduce {
                        state.copy (
                            tempWorkList = updateWorkList
                        )
                    }
                }
                reduce {
                    state.copy(
                        selectedDate = if(state.selectedDate == 1) state.selectedDate else state.selectedDate - 1,
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
            val response = editWorkScheduleUseCase("Bearer $accessToken", userInfo!!.workGroupId!!, state.editWorkList)
            if(response.isSuccess) {
                postSideEffect(MyCalendarSideEffect.Toast("등록되었습니다!"))
            } else {
                postSideEffect(MyCalendarSideEffect.Toast("등록에 실패하였습니다. 다시 시도해주세요."))
            }
        } else if (state.createWorkList.isNotEmpty()) {
            // TODO 근무 생성 요청 보내고 새로운 근무 일정 불러오기
            val response = createWorkScheduleUseCase("Bearer $accessToken", userInfo!!.workGroupId!!, state.createWorkList)
            if(response.isSuccess) {
                postSideEffect(MyCalendarSideEffect.Toast("등록되었습니다!"))
            } else {
                postSideEffect(MyCalendarSideEffect.Toast("등록에 실패하였습니다. 다시 시도해주세요."))
            }
        }

        getWorkList()
        updateCurrentWorkData()

        // 불러온 근무 일정을 monthlyWorkList 에 대입해주기
        reduce {
            state.copy(
//                monthlyWorkList = listOf(),
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
}

data class MyCalenderState(
    val selectedIdx: Int = 0,
    val scheduleTitle: String = "",
    val scheduleContent: String = "",
    val startTime: LocalDateTime? = null,
    val endTime: LocalDateTime? = null,
    val currentYear: Int = Calendar.getInstance().get(Calendar.YEAR),
    val currentMonth: Int = Calendar.getInstance().get(Calendar.MONTH) + 1,
    val selectedDate: Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
    val workTypeList: List<WorkType> = listOf(),
    val monthlyWorkList: List<WorkListData> = listOf(), // 서버로부터 조회한 월간 근무 일정
    val createWorkList: List<AddWorkData> = listOf(),
    val editWorkList: List<EditWorkData> = listOf(),
    val tempWorkList: List<Pair<Int, String>> = listOf(), // 근무 입력시 달력에 표시할 내용 (workDate, workTitle)
    val currentWorkData: WorkType ?= null
)

sealed interface MyCalendarSideEffect {
    class Toast(val message: String) : MyCalendarSideEffect

    data object NavigateToMainActivity : MyCalendarSideEffect

    data object NavigateToGroupActivity : MyCalendarSideEffect

    data object NavigateToSettingActivity : MyCalendarSideEffect

    data object NavigateToCommunityActivity : MyCalendarSideEffect

    data object NavigateToCalendarSettingScreen : MyCalendarSideEffect
}