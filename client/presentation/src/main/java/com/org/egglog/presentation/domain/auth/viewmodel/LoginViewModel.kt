package com.org.egglog.presentation.domain.auth.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.org.egglog.domain.auth.usecase.LoginUseCase
import com.org.egglog.domain.auth.usecase.SetTokenUseCase
import com.org.egglog.presentation.domain.auth.viewmodel.extend.loginWithKakao
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
    private val setTokenUseCase: SetTokenUseCase
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

    fun onAuthClick(context: Context, type: String) = intent {
        if(type === "KAKAO") {
            try {
                // 서비스 코드에서는 간단하게 로그인 요청하고 oAuthToken 을 받아올 수 있다.
                val oAuthToken = UserApiClient.loginWithKakao(context)
                Log.e("MainActivity", "beanbean > $oAuthToken")
            } catch (error: Throwable) {
                if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                    Log.e("MainActivity", "사용자가 명시적으로 취소")
                } else {
                    Log.e("MainActivity", "인증 에러 발생", error)
                }
            }
        }
//        val tokens = loginUseCase(type).getOrThrow()
//        Log.e("token?: ", tokens.toString())
//        if (tokens != null) {
//            setTokenUseCase(accessToken = tokens.accessToken, refreshToken =  tokens.refreshToken)
//        }
//        postSideEffect(LoginSideEffect.NavigateToMainActivity)
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