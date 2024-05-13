package com.org.egglog.presentation.domain.myCalendar.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.org.egglog.domain.auth.model.UserDetail
import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.domain.auth.usecase.GetUserStoreUseCase
import com.org.egglog.domain.myCalendar.model.WorkType
import com.org.egglog.domain.myCalendar.usecase.CreatePersonalScheduleUseCase
import com.org.egglog.domain.myCalendar.usecase.GetWorkTypeListUseCase
import com.org.egglog.presentation.domain.community.viewmodel.PostListSideEffect
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
    private val getUserStoreUseCase: GetUserStoreUseCase
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

    fun load() = intent {
        accessToken = getTokenUseCase().first!!

        val workTypeList = getWorkTypeListUseCase("Bearer $accessToken").getOrThrow()
        Log.e("MyCalendarViewModel", "근무타입리스트는 $workTypeList 입니다")

        userInfo = getUserStoreUseCase()

        reduce {
            state.copy(workTypeList = workTypeList)
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
        Log.e("MyCalendarViewModel", "변경된 시작 시간은 $startTime 입니다")
        reduce {
            state.copy(startTime = startTime)
        }
    }

    fun onChangeEndTime(endTime: LocalDateTime) = intent {
        Log.e("MyCalendarViewModel", "변경된 종료 시간은 $endTime 입니다")
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

    fun onDateClicked(clickedDate: Int) = intent {
        val now = LocalDateTime.now()

        reduce {
            state.copy(
                selectedDate = clickedDate
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
    val currentMonth: Int = Calendar.getInstance().get(Calendar.MONTH),
    val selectedDate: Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
    val workTypeList: List<WorkType> = listOf()
)

sealed interface MyCalendarSideEffect {
    class Toast(val message: String) : MyCalendarSideEffect

    data object NavigateToMainActivity : MyCalendarSideEffect

    data object NavigateToGroupActivity : MyCalendarSideEffect

    data object NavigateToSettingActivity : MyCalendarSideEffect

    data object NavigateToCommunityActivity : MyCalendarSideEffect

    data object NavigateToCalendarSettingScreen : MyCalendarSideEffect
}