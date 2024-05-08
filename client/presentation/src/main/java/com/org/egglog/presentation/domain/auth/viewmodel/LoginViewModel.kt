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
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.messaging.messaging
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.org.egglog.domain.auth.model.Refresh
import com.org.egglog.domain.auth.model.UserFcmTokenParam
import com.org.egglog.domain.auth.model.UserParam
import com.org.egglog.domain.auth.usecase.PostLoginUseCase
import com.org.egglog.domain.auth.usecase.GetUserUseCase
import com.org.egglog.domain.auth.usecase.SetTokenUseCase
import com.org.egglog.domain.auth.usecase.SetUserStoreUseCase
import com.org.egglog.domain.auth.usecase.UpdateUserFcmTokenUseCase
import com.org.egglog.presentation.domain.auth.extend.authenticateAndGetUserProfile
import com.org.egglog.presentation.domain.auth.extend.loginWithKakao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.tasks.await
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.annotation.concurrent.Immutable
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val postLoginUseCase: PostLoginUseCase,
    private val setTokenUseCase: SetTokenUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val setUserStoreUseCase: SetUserStoreUseCase,
    private val updateUserFcmTokenUseCase: UpdateUserFcmTokenUseCase
): ViewModel(), ContainerHost<LoginState, LoginSideEffect> {
    override val container: Container<LoginState, LoginSideEffect> = container(
        initialState = LoginState(),
        buildSettings = {
            this.exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                intent {
                    postSideEffect(LoginSideEffect.Toast(message = throwable.message.orEmpty()))
                    reduce {
                        state.copy(enabled = true)
                    }
                }
            }
        }
    )

    fun onKakaoClick(context: Context) = intent {
        reduce {
            state.copy(enabled = false)
        }
        try {
            if(UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                val user = UserApiClient.loginWithKakao(context)
                val tokens = postLoginUseCase("KAKAO", UserParam(
                    name = user.kakaoAccount?.profile?.nickname.orEmpty(),
                    email = user.kakaoAccount?.email.orEmpty(),
                    profileImgUrl = user.kakaoAccount?.profile?.profileImageUrl.orEmpty()
                )).getOrThrow()
                tokenUtilFunc("KAKAO", tokens)
            } else {
                val kakaoTalkInstallUrl = "market://details?id=com.kakao.talk"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(kakaoTalkInstallUrl))
                context.startActivity(intent)
            }
            reduce {
                state.copy(enabled = true)
            }
        } catch (error: Throwable) {
            if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                Log.e("MainActivity", "사용자가 명시적으로 취소")
                postSideEffect(LoginSideEffect.Toast("취소 되었습니다."))
                reduce {
                    state.copy(enabled = true)
                }
            } else {
                Log.e("MainActivity", "인증 에러 발생: ${error.message}")
                postSideEffect(LoginSideEffect.Toast("인증 에러가 발생했습니다."))
                reduce {
                    state.copy(enabled = true)
                }
            }
        }
    }

    fun onNaverClick(context: Context) = intent {
        reduce {
            state.copy(enabled = false)
        }
        val user = authenticateAndGetUserProfile(context)
        val tokens = postLoginUseCase("NAVER", UserParam(
            name = user.profile?.name.orEmpty(),
            email = user.profile?.email.orEmpty(),
            profileImgUrl = user.profile?.profileImage.orEmpty()
        )).getOrThrow()
        tokenUtilFunc("NAVER", tokens)
    }

    fun onGoogleUserReceived(user: FirebaseUser?) = intent {
        reduce {
            state.copy(enabled = false)
        }
        val tokens = postLoginUseCase("GOOGLE", UserParam(
            name = user?.displayName.orEmpty(),
            email = user?.email.orEmpty(),
            profileImgUrl = user?.photoUrl.toString()
        )).getOrThrow()
        tokenUtilFunc("GOOGLE", tokens)
    }

    fun onGoogleClick(context: Context, launcher: ManagedActivityResultLauncher<Intent, ActivityResult>, token: String) = intent {
        reduce {
            state.copy(enabled = false)
        }
        val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(token)
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(context, gso)
        launcher.launch(googleSignInClient.signInIntent)
    }

    private fun tokenUtilFunc(type: String, tokens: Refresh?) = intent {
        if(tokens?.accessToken?.isNotEmpty() == true && tokens.accessToken.isNotEmpty() && tokens.refreshToken.isNotEmpty()) {
            Log.e("$type > AccessToken : ", tokens.accessToken)
            Log.e("$type > RefreshToken : ", tokens.refreshToken)
            setTokenUseCase(
                accessToken = tokens.accessToken,
                refreshToken = tokens.refreshToken
            )
            val userDetail = getUserUseCase("Bearer ${tokens.accessToken}").getOrThrow()
            if(userDetail?.selectedHospital == null || userDetail.empNo == null) {
                setUserStoreUseCase(userDetail)
                postSideEffect(LoginSideEffect.NavigateToPlusLoginActivity)
            } else {
                val fcmToken = try {
                    Firebase.messaging.token.await()
                } catch (e: Exception) {
                    Log.e("FCM Token", "Error fetching FCM token: ${e.message}", e)
                    ""
                }
                if(userDetail.deviceToken == null || userDetail.deviceToken != fcmToken) {
                    val newUser = updateUserFcmTokenUseCase(UserFcmTokenParam(fcmToken)).getOrThrow()
                    setUserStoreUseCase(newUser)
                } else setUserStoreUseCase(userDetail)
                postSideEffect(LoginSideEffect.NavigateToMainActivity)
            }
            reduce {
                state.copy(enabled = true)
            }
        } else {
            postSideEffect(LoginSideEffect.Toast("인증 에러가 발생했습니다."))
            reduce {
                state.copy(enabled = true)
            }
        }
    }
}

@Immutable
data class LoginState(
    val enabled: Boolean = true
)

sealed interface LoginSideEffect {
    class Toast(val message: String) : LoginSideEffect
    data object NavigateToMainActivity:LoginSideEffect
    data object NavigateToPlusLoginActivity:LoginSideEffect
}