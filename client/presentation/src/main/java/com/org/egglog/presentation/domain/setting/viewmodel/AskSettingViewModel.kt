package com.org.egglog.presentation.domain.setting.viewmodel

import androidx.lifecycle.ViewModel
import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.domain.auth.usecase.GetUserStoreUseCase
import com.org.egglog.domain.setting.model.AskParam
import com.org.egglog.domain.setting.usecase.PostAskUseCase
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
class AskSettingViewModel @Inject constructor(
    private val getTokenUseCase: GetTokenUseCase,
    private val getUserStoreUseCase: GetUserStoreUseCase,
    private val postAskUseCase: PostAskUseCase
): ViewModel(), ContainerHost<AskSettingState, AskSettingSideEffect>{
    override val container: Container<AskSettingState, AskSettingSideEffect> = container(
        initialState = AskSettingState(),
        buildSettings = {
            this.exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                intent {
                    postSideEffect(AskSettingSideEffect.Toast(message = throwable.message.orEmpty()))
                    reduce { state.copy(sendEnabled = true) }
                }
            }
        }
    )

    init {
        load()
    }

    private fun load() = intent {
        val user = getUserStoreUseCase()
        reduce { state.copy(placeholderEmail = user?.email ?: "") }
    }

    fun onClickSend() = intent {
        reduce { state.copy(sendEnabled = false) }
        val tokens = getTokenUseCase()
        postAskUseCase(accessToken = "Bearer ${tokens.first.orEmpty()}", AskParam(email = if(state.email.isEmpty()) null else state.email, content = state.content, title = state.title)).getOrThrow()
        reduce { state.copy(sendEnabled = true) }
    }

    @OptIn(OrbitExperimental::class)
    fun onTitleChange(title: String) = blockingIntent {
        reduce { state.copy(title = title) }
    }

    @OptIn(OrbitExperimental::class)
    fun onEmailChange(email: String) = blockingIntent {
        reduce { state.copy(email = email) }
    }

    @OptIn(OrbitExperimental::class)
    fun onContentChange(content: String) = blockingIntent {
        reduce { state.copy(content = content) }
    }
}

@Immutable
data class AskSettingState(
    val title: String = "",
    val email: String = "",
    val placeholderEmail: String = "",
    val content: String = "",
    val sendEnabled: Boolean = true
)

sealed interface AskSettingSideEffect {
    class Toast(val message: String): AskSettingSideEffect
}