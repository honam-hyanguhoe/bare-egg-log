package com.org.egglog.presentation.domain.auth.viewmodel

import androidx.lifecycle.ViewModel
import com.org.egglog.domain.auth.usecase.GetGoogleUseCase
import com.org.egglog.domain.auth.usecase.GetKakaoUseCase
import com.org.egglog.domain.auth.usecase.LoginUseCase
import com.org.egglog.domain.auth.usecase.SetTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.viewmodel.container
import javax.annotation.concurrent.Immutable
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val setTokenUseCase: SetTokenUseCase,
    private val getKakaoUseCase: GetKakaoUseCase,
    private val getGoogleUseCase: GetGoogleUseCase
): ViewModel(), ContainerHost<LoginState, LoginSideEffect> {
    override val container: Container<LoginState, LoginSideEffect> = container(
        initialState = LoginState(),
        buildSettings = {
            this.exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                intent {
//                    postSideEffect(LoginSideEffect.Toast(message = throwable.message.orEmpty()))
                    postSideEffect(LoginSideEffect.NavigateToMainActivity)
                }
            }
        }
    )

    fun onKakaoClick() = intent {
        getKakaoUseCase()
    }
}

@Immutable
data class LoginState(
    val type: String = ""
)

sealed interface LoginSideEffect {
    class Toast(val message: String) : LoginSideEffect
    data object NavigateToMainActivity:LoginSideEffect
}