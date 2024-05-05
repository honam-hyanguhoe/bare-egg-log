package com.org.egglog.presentation.domain.auth.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseUser
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.org.egglog.domain.auth.model.UserParam
import com.org.egglog.domain.auth.usecase.GetUserUseCase
import com.org.egglog.domain.auth.usecase.LoginUseCase
import com.org.egglog.domain.auth.usecase.SetTokenUseCase
import com.org.egglog.presentation.domain.auth.extend.authenticateAndGetUserProfile
import com.org.egglog.presentation.domain.auth.extend.loginWithKakao
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
    private val getUserUseCase: GetUserUseCase
): ViewModel(), ContainerHost<LoginState, LoginSideEffect> {
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

    fun onKakaoClick(context: Context) = intent {
        try {
            if(UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                val user = UserApiClient.loginWithKakao(context)
                val tokens = loginUseCase("kakao", UserParam(
                    name = user.kakaoAccount?.profile?.nickname.orEmpty(),
                    email = user.kakaoAccount?.email.orEmpty(),
                    profileImgUrl = user.kakaoAccount?.profile?.profileImageUrl.orEmpty()
                )).getOrThrow()
                if(tokens?.accessToken?.isNotEmpty() == true && tokens.accessToken.isNotEmpty() && tokens.refreshToken.isNotEmpty()) {
                    setTokenUseCase(
                        accessToken = tokens.accessToken,
                        refreshToken = tokens.refreshToken
                    )
                    val userDetail = getUserUseCase("Bearer ${tokens.accessToken}").getOrThrow()
                    if(userDetail?.hospitalAuth == null || userDetail.empNo == null) {
                        postSideEffect(LoginSideEffect.NavigateToPlusLoginActivity)
                    } else {
                        postSideEffect(LoginSideEffect.NavigateToMainActivity)
                    }
                } else {
                    postSideEffect(LoginSideEffect.Toast("인증 에러가 발생했습니다."))
                }
            } else {
                val kakaoTalkInstallUrl = "market://details?id=com.kakao.talk"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(kakaoTalkInstallUrl))
                context.startActivity(intent)
            }
        } catch (error: Throwable) {
            if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                Log.e("MainActivity", "사용자가 명시적으로 취소")
                postSideEffect(LoginSideEffect.Toast("취소 되었습니다."))
            } else {
                Log.e("MainActivity", "인증 에러 발생: ${error.message}")
                postSideEffect(LoginSideEffect.Toast("인증 에러가 발생했습니다."))
            }
        }
    }

    fun onNaverClick(context: Context) = intent {
        val user = authenticateAndGetUserProfile(context)
        val tokens = loginUseCase("naver", UserParam(
            name = user.profile?.name.orEmpty(),
            email = user.profile?.email.orEmpty(),
            profileImgUrl = user.profile?.profileImage.orEmpty()
        )).getOrThrow()
        if(tokens?.accessToken?.isNotEmpty() == true && tokens.accessToken.isNotEmpty() && tokens.refreshToken.isNotEmpty()) {
            setTokenUseCase(
                accessToken = tokens.accessToken,
                refreshToken = tokens.refreshToken
            )
            val userDetail = getUserUseCase("Bearer ${tokens.accessToken}").getOrThrow()
            Log.e("userDetail: ", userDetail?.userName.orEmpty())
            if(userDetail?.hospitalAuth == null || userDetail.empNo == null) {
                postSideEffect(LoginSideEffect.NavigateToPlusLoginActivity)
            } else {
                postSideEffect(LoginSideEffect.NavigateToMainActivity)
            }
        } else {
            postSideEffect(LoginSideEffect.Toast("인증 에러가 발생했습니다."))
        }
    }

    fun onGoogleUserReceived(user: FirebaseUser?) = intent {
        val tokens = loginUseCase("google", UserParam(
            name = user?.displayName.orEmpty(),
            email = user?.email.orEmpty(),
            profileImgUrl = user?.photoUrl.toString()
        )).getOrThrow()
        if(tokens?.accessToken?.isNotEmpty() == true && tokens.accessToken.isNotEmpty() && tokens.refreshToken.isNotEmpty()) {
            setTokenUseCase(
                accessToken = tokens.accessToken,
                refreshToken = tokens.refreshToken
            )
            val userDetail = getUserUseCase("Bearer ${tokens.accessToken}").getOrThrow()
            if(userDetail?.hospitalAuth == null || userDetail.empNo == null) {
                postSideEffect(LoginSideEffect.NavigateToPlusLoginActivity)
            } else {
                postSideEffect(LoginSideEffect.NavigateToMainActivity)
            }
        } else {
            postSideEffect(LoginSideEffect.Toast("인증 에러가 발생했습니다."))
        }
    }

    fun onGoogleClick(context: Context, launcher: ManagedActivityResultLauncher<Intent, ActivityResult>, token: String) = intent {
        val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(token)
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(context, gso)
        launcher.launch(googleSignInClient.signInIntent)
    }
}

@Immutable
data class LoginState(
    val type: String = ""
)

sealed interface LoginSideEffect {
    class Toast(val message: String) : LoginSideEffect
    data object NavigateToMainActivity:LoginSideEffect
    data object NavigateToPlusLoginActivity:LoginSideEffect
}