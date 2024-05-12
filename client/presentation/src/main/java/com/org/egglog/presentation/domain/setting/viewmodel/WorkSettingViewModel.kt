package com.org.egglog.presentation.domain.setting.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.domain.auth.usecase.GetUserStoreUseCase
import com.org.egglog.domain.setting.model.WorkType
import com.org.egglog.domain.setting.usecase.GetWorkTypeListUseCase
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
class WorkSettingViewModel @Inject constructor(
    private val getTokenUseCase: GetTokenUseCase,
    private val getUserStoreUseCase: GetUserStoreUseCase,
    private val getWorkTypeListUseCase: GetWorkTypeListUseCase
): ViewModel(), ContainerHost<WorkSettingState, WorkSettingSideEffect>{
    override val container: Container<WorkSettingState, WorkSettingSideEffect> = container(
        initialState = WorkSettingState(),
        buildSettings = {
            this.exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                intent {
                    postSideEffect(WorkSettingSideEffect.Toast(message = throwable.message.orEmpty()))
                    reduce { state.copy(deleteEnabled = true, addEnabled = true) }
                }
            }
        }
    )

    fun getWorkListInit() = intent {
        val tokens = getTokenUseCase()
        val workTypeList = getWorkTypeListUseCase(accessToken = "Bearer ${tokens.first.orEmpty()}").getOrThrow()
        reduce { state.copy(workTypeList = workTypeList ?: emptyList()) }
    }

    fun onClickDelete(calendarGroupId: Long) = intent {
        reduce { state.copy(deleteEnabled = false) }
        val tokens = getTokenUseCase()

        reduce { state.copy(deleteEnabled = true) }
    }

    fun onClickAdd() = intent {
        reduce { state.copy(addEnabled = false) }
        val tokens = getTokenUseCase()

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
data class WorkSettingState(
    val workTypeList: List<WorkType> = emptyList(),
    val addEnabled: Boolean = true,
    val deleteEnabled: Boolean = true,
    val url: String = "",
    val alias: String = ""
)

sealed interface WorkSettingSideEffect {
    class Toast(val message: String): WorkSettingSideEffect
}