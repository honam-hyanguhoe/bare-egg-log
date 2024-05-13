package com.org.egglog.presentation.domain.group.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.domain.auth.usecase.GetUserStoreUseCase
import com.org.egglog.domain.group.model.Group
import com.org.egglog.domain.group.usecase.CreateGroupUseCase
import com.org.egglog.domain.group.usecase.GetGroupListUseCase
import com.org.egglog.presentation.component.organisms.calendars.weeklyData.WeeklyDataSource
import com.org.egglog.presentation.component.organisms.calendars.weeklyData.WeeklyUiModel
import com.org.egglog.presentation.domain.main.viewModel.StaticSideEffect
import com.org.egglog.presentation.domain.main.viewModel.StaticState
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
import java.security.PrivilegedAction
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class GroupListViewModel @Inject constructor(
    private val getTokenUseCase: GetTokenUseCase,
    private val getGroupListUseCase: GetGroupListUseCase,
    private val createGroupUseCase: CreateGroupUseCase,
    private val getUserStoreUseCase: GetUserStoreUseCase
) : ViewModel(), ContainerHost<GroupListState, GroupListSideEffect> {
    override val container: Container<GroupListState, GroupListSideEffect> =
        container(initialState = GroupListState(), buildSettings = {
            this.exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                intent {
                    postSideEffect(GroupListSideEffect.Toast(message = throwable.message.orEmpty()))
                }
            }
        })

    init {
        getGroupList()
        // state 값의 groupImage 번호 설정
    }

    private val _showBottomSheet = MutableStateFlow(false)
    val showBottomSheet: StateFlow<Boolean> = _showBottomSheet.asStateFlow()

    fun setShowBottomSheet(show: Boolean) {
        _showBottomSheet.value = show
    }

    fun createGroup() = intent {

        val tokens = getTokenUseCase()

        Log.d("write group", "${tokens.first}")
        Log.d("write group", "등록 ${state.groupName}, ${state.groupPassword}, ${state.groupImage}")

        val result = createGroupUseCase(
            accessToken = "Bearer ${tokens.first}",
            groupName = state.groupName,
            groupPassword = state.groupPassword,
            groupImage = state.groupImage
        )
        val groupImageNumber =  (state.groupImage ) % 3 + 1
        val newGroup = Group(
            groupId = state.groupId + 1,
            groupName = state.groupName,
            admin = getUserStoreUseCase()?.userName ?: "",
            groupImage = groupImageNumber,
            memberCount = 1
        )
        reduce {
            state.copy(
                groupId = state.groupId + 1,
                groupName = "",
                groupPassword = "",
                // 프론트 state 값만 추가 -> 화면 재 접속할 때만 api 호출
                groupList = state.groupList.plus(newGroup),
                groupImage = groupImageNumber
            )
        }
        Log.d("write group", state.groupList.toString())
    }

    @OptIn(OrbitExperimental::class)
    fun onChangeGroupName(value: String) = blockingIntent {
        reduce {
            state.copy(
                groupName = value
            )
        }
    }


    @OptIn(OrbitExperimental::class)
    fun onChangeGroupPassword(value: String) = blockingIntent {
        reduce {
            state.copy(
                groupPassword = value
            )
        }
    }


    private fun getGroupList() = intent {
        val tokens = getTokenUseCase()

        Log.d("groupList", "${tokens.first}")
        val result = getGroupListUseCase(
            accessToken = "Bearer ${tokens.first}"
        ).getOrNull()
        Log.d("groupList", "ddd ${result.toString()}")

        reduce {
            val lastGroup = result?.lastOrNull()
            state.copy(
                groupList = result ?: emptyList(),
                groupImage = lastGroup?.groupImage ?: 1,
                groupId = lastGroup?.groupId ?: 0
            )
        }
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
    val groupList: List<Group> = emptyList(),
    val statsData: String = "",
    val selectedIdx: Int = 1,
    val groupName: String = "",
    val groupPassword: String = "",
    val groupImage: Int = 1,
    val groupId: Long = 0,
)

sealed class GroupListSideEffect {
    class Toast(val message: String) : GroupListSideEffect()
    object NavigateToCalendarScreen : GroupListSideEffect()
    object NavigateToHomeScreen : GroupListSideEffect()
    object NavigateToCommunityScreen : GroupListSideEffect()
    object NavigateToSettingScreen : GroupListSideEffect()
}

