package com.org.egglog.presentation.domain.auth.viewmodel

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.org.egglog.domain.auth.usecase.GetGoogleUseCase
import com.org.egglog.domain.auth.usecase.GetKakaoUseCase
import com.org.egglog.domain.auth.usecase.GetNaverUseCase
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
    private val getNaverUseCase: GetNaverUseCase,
    private val getGoogleUseCase: GetGoogleUseCase
): ViewModel(), ContainerHost<LoginState, LoginSideEffect> {
    private lateinit var oauthToken: String
    override val container: Container<LoginState, LoginSideEffect> = container(
        initialState = LoginState(),
        buildSettings = {
            this.exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                intent {
                    postSideEffect(LoginSideEffect.Toast(message = throwable.message.orEmpty()))
                }
            }
        }
    )

    private val oAuthLoginCallback = object : OAuthLoginCallback {
        override fun onSuccess() {}
        override fun onError(errorCode: Int, message: String) {
            onFailure(errorCode, message)
        }

        override fun onFailure(httpStatus: Int, message: String) {
            val errorCode = NaverIdLoginSDK.getLastErrorCode().code
            val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
            Log.e("naver error: ", "$errorCode $errorDescription")
        }
    }

    fun onKakaoClick() = intent {
        val tokens = getKakaoUseCase().getOrThrow()
        if(tokens.accessToken.isNotEmpty() && tokens.refreshToken.isNotEmpty()) {
            postSideEffect(LoginSideEffect.NavigateToMainActivity)
        } else {
            postSideEffect(LoginSideEffect.Toast("로그인에 실패했습니다."))
        }
        postSideEffect(LoginSideEffect.NavigateToMainActivity)
    }

    fun onNaverClick(context: Context) = intent {
        NaverIdLoginSDK.authenticate(context, oAuthLoginCallback)
        Log.e("naver Token: ", NaverIdLoginSDK.getAccessToken().orEmpty())
        oauthToken = NaverIdLoginSDK.getAccessToken().orEmpty()
        val tokens = getNaverUseCase().getOrThrow()
        if(tokens.accessToken.isNotEmpty() && tokens.refreshToken.isNotEmpty()) {
            postSideEffect(LoginSideEffect.NavigateToMainActivity)
        } else {
            postSideEffect(LoginSideEffect.Toast("로그인에 실패했습니다."))
        }
    }

    fun onGoogleAccountReceived(account: GoogleSignInAccount) {
        Log.e("google Token", account.idToken.orEmpty())
        oauthToken = account.idToken.orEmpty()
    }

    fun onGoogleClick(context: Context, launcher: ManagedActivityResultLauncher<Intent, ActivityResult>, token: String) = intent {
        val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(token)
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(context, gso)
        launcher.launch(googleSignInClient.signInIntent)
        val tokens = getGoogleUseCase().getOrThrow()
        if(tokens.accessToken.isNotEmpty() && tokens.refreshToken.isNotEmpty()) {
            postSideEffect(LoginSideEffect.NavigateToMainActivity)
        } else {
            postSideEffect(LoginSideEffect.Toast("로그인에 실패했습니다."))
        }
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