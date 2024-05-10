package com.org.egglog.presentation.domain.group.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.domain.group.model.Group
import com.org.egglog.domain.group.usecase.GetGroupListUseCase
import com.org.egglog.presentation.component.organisms.calendars.weeklyData.WeeklyDataSource
import com.org.egglog.presentation.component.organisms.calendars.weeklyData.WeeklyUiModel
import com.org.egglog.presentation.domain.main.viewModel.StaticSideEffect
import com.org.egglog.presentation.domain.main.viewModel.StaticState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.viewmodel.container
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class GroupListViewModel @Inject constructor(
    private val getTokenUseCase: GetTokenUseCase,
    private val getGroupListUseCase: GetGroupListUseCase
) : ViewModel(), ContainerHost<GroupListState, GroupListSideEffect> {
    override val container: Container<GroupListState, GroupListSideEffect> = container(
        initialState = GroupListState(),
        buildSettings = {
            this.exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                intent {
                    postSideEffect(GroupListSideEffect.Toast(message = throwable.message.orEmpty()))
                }
            }
        })

    init {
        getGroupList()
    }

    private fun getGroupList() = intent {
        val tokens = getTokenUseCase()

        val result = getGroupListUseCase(
            accessToken = "Bearer ${tokens.first}"
        )
        Log.d("groupList", result.toString())
    }

    fun onSelectedIdx(selectedIdx: Int) = intent {
        when (selectedIdx) {
            0 -> postSideEffect(GroupListSideEffect.NavigateToCalendarScreen)
            2 -> postSideEffect(GroupListSideEffect.NavigateToHomeScreen)
            3 -> postSideEffect(GroupListSideEffect.NavigateToCommunityScreen)
            4 -> postSideEffect(GroupListSideEffect.NavigateToSettingScreen)
        }
    }
}

data class GroupListState(
    val groupList: List<Group> = listOf(
        Group(id = 1, master = "김다희", name = "호남 향우회", memberCount = 2, image = 1),
        Group(id = 2, master = "박철수", name = "서울 친구들", memberCount = 5, image = 2),
        Group(id = 3, master = "이영희", name = "동네 친구들", memberCount = 3, image = 3),
        Group(id = 1, master = "김다희", name = "호남 향우회", memberCount = 2, image = 1),
        Group(id = 2, master = "박철수", name = "서울 친구들", memberCount = 5, image = 2),
        Group(id = 3, master = "이영희", name = "동네 친구들", memberCount = 3, image = 3),
        Group(id = 1, master = "김다희", name = "호남 향우회", memberCount = 2, image = 1),
        Group(id = 2, master = "박철수", name = "서울 친구들", memberCount = 5, image = 2),
        Group(id = 3, master = "이영희", name = "동네 친구들", memberCount = 3, image = 3),
        Group(id = 1, master = "김다희", name = "호남 향우회", memberCount = 2, image = 1),
        Group(id = 2, master = "박철수", name = "서울 친구들", memberCount = 5, image = 2),
        Group(id = 3, master = "이영희", name = "동네 친구들", memberCount = 3, image = 3),

        ),
    val statsData: String = "",
    val selectedIdx: Int = 2
)

sealed class GroupListSideEffect {
    class Toast(val message: String) : GroupListSideEffect()
    object NavigateToCalendarScreen : GroupListSideEffect()
    object NavigateToHomeScreen : GroupListSideEffect()
    object NavigateToCommunityScreen : GroupListSideEffect()
    object NavigateToSettingScreen : GroupListSideEffect()
}

