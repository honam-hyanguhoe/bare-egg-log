package com.org.egglog.presentation.domain.setting.viewmodel

import androidx.lifecycle.ViewModel
import com.org.egglog.domain.auth.model.UserDetail
import com.org.egglog.domain.setting.usecase.GetCalendarGroupMapStoreUseCase
import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.domain.auth.usecase.GetUserStoreUseCase
import com.org.egglog.domain.setting.model.AddCalendarGroupParam
import com.org.egglog.domain.setting.model.CalendarGroup
import com.org.egglog.domain.setting.usecase.DeleteCalendarGroupUseCase
import com.org.egglog.domain.setting.usecase.GetCalendarGroupListUseCase
import com.org.egglog.domain.setting.usecase.PostCalendarGroupUseCase
import com.org.egglog.domain.setting.usecase.PostCalendarSyncUseCase
import com.org.egglog.domain.setting.usecase.SetCalendarGroupMapStoreUseCase
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
import javax.annotation.concurrent.Immutable
import javax.inject.Inject

@HiltViewModel
class CalendarSettingViewModel @Inject constructor(
    private val getTokenUseCase: GetTokenUseCase,
    private val getUserStoreUseCase: GetUserStoreUseCase,
    private val getCalendarGroupMapStoreUseCase: GetCalendarGroupMapStoreUseCase,
    private val setCalendarGroupMapStoreUseCase: SetCalendarGroupMapStoreUseCase,
    private val deleteCalendarGroupUseCase: DeleteCalendarGroupUseCase,
    private val getCalendarGroupListUseCase: GetCalendarGroupListUseCase,
    private val postCalendarGroupUseCase: PostCalendarGroupUseCase,
    private val postCalendarSyncUseCase: PostCalendarSyncUseCase
): ViewModel(), ContainerHost<CalendarSettingState, CalendarSettingSideEffect>{
    override val container: Container<CalendarSettingState, CalendarSettingSideEffect> = container(
        initialState = CalendarSettingState(),
        buildSettings = {
            this.exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                intent {
                    postSideEffect(CalendarSettingSideEffect.Toast(message = throwable.message.orEmpty()))
                    reduce { state.copy(deleteEnabled = true, syncEnabled = true, addEnabled = true) }
                }
            }
        }
    )

    fun getCalendarListInit() = intent {
        val tokens = getTokenUseCase()
        val calendarGroupList = getCalendarGroupListUseCase(accessToken = "Bearer ${tokens.first.orEmpty()}").getOrThrow()
        val user = getUserStoreUseCase()
        val calendarGroupMapStore = getCalendarGroupMapStoreUseCase()
        val map: MutableMap<String, Boolean> = mutableMapOf()

        val updateList = calendarGroupList?.map { calendarGroup ->
            val isEnabled = if(calendarGroupMapStore.contains(calendarGroup.calendarGroupId.toString())) {
                calendarGroupMapStore[calendarGroup.calendarGroupId.toString()] == true
            } else { false }
            val isBasic = calendarGroup.calendarGroupId == user?.workGroupId
            calendarGroup.copy(
                isEnabled = isEnabled || isBasic,
                isBasic = isBasic
            )
        }

        setCalendarGroupMapStoreUseCase(map)
        reduce { state.copy(calendarGroupList = updateList ?: emptyList(), user = user) }
    }

    fun onClickSync() = intent {
        reduce {
            state.copy(syncEnabled = false)
        }
        val tokens = getTokenUseCase()
        postCalendarSyncUseCase(accessToken = "Bearer ${tokens.first.orEmpty()}").getOrThrow()
        reduce { state.copy(syncEnabled = true) }
    }

    fun onClickToggle(calendarGroupId: Long) = intent {
        val map = getCalendarGroupMapStoreUseCase().toMutableMap().apply {
            this[calendarGroupId.toString()] = !(this[calendarGroupId.toString()] ?: false)
        }
        setCalendarGroupMapStoreUseCase(map)
        val updatedList = state.calendarGroupList.map { group ->
            if (group.calendarGroupId == calendarGroupId) {
                group.copy(isEnabled = map[group.calendarGroupId.toString()] == true)
            } else {
                group
            }
        }
        reduce { state.copy(calendarGroupList = updatedList) }
    }

    fun onClickDelete(calendarGroupId: Long) = intent {
        reduce { state.copy(deleteEnabled = false) }
        val tokens = getTokenUseCase()
        deleteCalendarGroupUseCase(calendarGroupId = calendarGroupId, accessToken = "Bearer ${tokens.first.orEmpty()}").getOrThrow()
        reduce { state.copy(deleteEnabled = true) }
    }

    fun onClickAdd() = intent {
        reduce { state.copy(addEnabled = false) }
        val tokens = getTokenUseCase()
        postCalendarGroupUseCase(accessToken = "Bearer ${tokens.first.orEmpty()}", addCalendarGroupParam = AddCalendarGroupParam(alias = state.alias, url = state.url.ifEmpty { null }, inUrl = state.url.isNotEmpty())).getOrThrow()
        postSideEffect(CalendarSettingSideEffect.Toast("추가 되었습니다."))
        reduce { state.copy(addEnabled = true) }
    }

    @OptIn(OrbitExperimental::class)
    fun onUrlChange(url: String) = blockingIntent {
        reduce { state.copy(url = url) }
    }

    @OptIn(OrbitExperimental::class)
    fun onAliasChange(alias: String) = blockingIntent {
        reduce { state.copy(alias = alias) }
    }
}

@Immutable
data class CalendarSettingState(
    val syncEnabled: Boolean = true,
    val addEnabled: Boolean = true,
    val deleteEnabled: Boolean = true,
    val calendarGroupList: List<CalendarGroup> = emptyList(),
    val url: String = "",
    val alias: String = "",
    val user: UserDetail? = null
)

sealed interface CalendarSettingSideEffect {
    class Toast(val message: String): CalendarSettingSideEffect
}