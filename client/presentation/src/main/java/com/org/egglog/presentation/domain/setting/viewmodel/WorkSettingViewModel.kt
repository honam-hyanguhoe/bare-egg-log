package com.org.egglog.presentation.domain.setting.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.domain.setting.model.WorkType
import com.org.egglog.domain.setting.model.WorkTypeParam
import com.org.egglog.domain.setting.usecase.DeleteWorkTypeUseCase
import com.org.egglog.domain.setting.usecase.GetWorkTypeListUseCase
import com.org.egglog.domain.setting.usecase.PostWorkTypeUseCase
import com.org.egglog.domain.setting.usecase.UpdateWorkTypeUseCase
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
import java.time.LocalTime
import javax.annotation.concurrent.Immutable
import javax.inject.Inject

@HiltViewModel
class WorkSettingViewModel @Inject constructor(
    private val getTokenUseCase: GetTokenUseCase,
    private val getWorkTypeListUseCase: GetWorkTypeListUseCase,
    private val postWorkTypeUseCase: PostWorkTypeUseCase,
    private val updateWorkTypeUseCase: UpdateWorkTypeUseCase,
    private val deleteWorkTypeUseCase: DeleteWorkTypeUseCase
): ViewModel(), ContainerHost<WorkSettingState, WorkSettingSideEffect>{
    override val container: Container<WorkSettingState, WorkSettingSideEffect> = container(
        initialState = WorkSettingState(),
        buildSettings = {
            this.exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                intent {
                    postSideEffect(WorkSettingSideEffect.Toast(message = throwable.message.orEmpty()))
                    reduce { state.copy(deleteEnabled = true, addEnabled = true, modifyEnabled = true) }
                }
            }
        }
    )

    fun setShowModifyBottomSheet(enabled: Boolean) = intent {
        reduce { state.copy(showModifyBottomSheet = enabled) }
    }

    fun setShowCreateBottomSheet(enabled: Boolean) = intent {
        reduce { state.copy(showCreateBottomSheet = enabled) }
    }

    fun getWorkListInit() = intent {
        val tokens = getTokenUseCase()
        val workTypeList = getWorkTypeListUseCase(accessToken = "Bearer ${tokens.first.orEmpty()}").getOrThrow()
        reduce { state.copy(workTypeList = workTypeList ?: emptyList()) }
    }

    fun onClickAdd() = intent {
        reduce { state.copy(addEnabled = false) }
        val tokens = getTokenUseCase()
        Log.e("test", "${state.title} ${state.color}")
        postWorkTypeUseCase(accessToken = "Bearer ${tokens.first.orEmpty()}", workTypeParam = WorkTypeParam(title = state.title, color = state.color, workTypeImgUrl = "", startTime = state.startTime, workTime = state.endTime)).getOrThrow()
        reduce { state.copy(title = "", color = "", showCreateBottomSheet = false, addEnabled = true) }
    }

    fun onClickModify() = intent {
        reduce { state.copy(modifyEnabled = false) }
        val tokens = getTokenUseCase()
        updateWorkTypeUseCase(accessToken = "Bearer ${tokens.first.orEmpty()}", workTypeId = state.selectedWorkType?.workTypeId ?: 0L, workTypeParam = WorkTypeParam(title = state.title, color = state.color, workTypeImgUrl = state.selectedWorkType?.workTypeImgUrl, startTime = state.startTime, workTime = state.endTime)).getOrThrow()
        postSideEffect(WorkSettingSideEffect.Toast("수정 되었습니다."))
        reduce { state.copy(title = "", color = "", showModifyBottomSheet = false, modifyEnabled = true, selectedWorkType = null) }
    }

    fun onSelected(selected: String, workType: WorkType) = intent {
        val tokens = getTokenUseCase()
        if(selected == "삭제") {
            reduce { state.copy(addEnabled = false) }
            deleteWorkTypeUseCase(accessToken = "Bearer ${tokens.first.orEmpty()}", workTypeId = workType.workTypeId).getOrThrow()
            postSideEffect(WorkSettingSideEffect.Toast("삭제 되었습니다."))
            reduce { state.copy(addEnabled = true) }
        } else if(selected == "수정") {
            reduce { state.copy(selectedWorkType = workType, showModifyBottomSheet = true, title = workType.title, color =  workType.color) }
        }
    }

    @OptIn(OrbitExperimental::class)
    fun onTitleChange(title: String) = blockingIntent {
        reduce { state.copy(title = title) }
    }

    @OptIn(OrbitExperimental::class)
    fun deleteSelectedWorkType() = blockingIntent {
        reduce { state.copy(selectedWorkType = null) }
    }

    @OptIn(OrbitExperimental::class)
    fun onColorChange(alias: String) = blockingIntent {
        reduce { state.copy(color = alias) }
    }

    @OptIn(OrbitExperimental::class)
    fun onStartTimeChange(startTime: LocalTime) = blockingIntent {
        reduce { state.copy(startTime = startTime) }
    }

    @OptIn(OrbitExperimental::class)
    fun onEndTimeChange(endTime: LocalTime) = blockingIntent {
        reduce { state.copy(endTime = endTime) }
    }
}

@Immutable
data class WorkSettingState(
    val workTypeList: List<WorkType> = emptyList(),
    val showCreateBottomSheet: Boolean = false,
    val showModifyBottomSheet: Boolean = false,
    val addEnabled: Boolean = true,
    val modifyEnabled: Boolean = true,
    val deleteEnabled: Boolean = true,
    val title: String = "",
    val color: String = "",
    val startTime: LocalTime = LocalTime.now(),
    val endTime: LocalTime = LocalTime.now(),
    val selectedWorkType: WorkType? = null
)

sealed interface WorkSettingSideEffect {
    class Toast(val message: String): WorkSettingSideEffect
}