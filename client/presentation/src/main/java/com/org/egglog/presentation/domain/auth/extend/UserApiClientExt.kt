package com.org.egglog.presentation.domain.auth.extend

import android.content.Context
import android.util.Log
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun UserApiClient.Companion.loginWithKakao(context: Context): User {
    return try {
        UserApiClient.loginWithKakaoTalk(context)
        return UserApiClient.profile(context)
    } catch (error: Throwable) {
        throw error
    }
}

/**
 * 카카오톡으로 로그인 시도
 */
suspend fun UserApiClient.Companion.loginWithKakaoTalk(context: Context): OAuthToken {
    return suspendCoroutine<OAuthToken> { continuation ->
        instance.loginWithKakaoTalk(context) { token, error ->
            if (error != null) {
                continuation.resumeWithException(error)
            } else if (token != null) {
                continuation.resume(token)
            } else {
                continuation.resumeWithException(RuntimeException("kakao access token을 받아오는데 실패함, 이유는 명확하지 않음."))
            }
        }
    }
}

/**
 * 카카오 계정으로 로그인 시도
 */
//suspend fun UserApiClient.Companion.loginWithKakaoAccount(context: Context): OAuthToken {
//    return suspendCoroutine<OAuthToken> { continuation ->
//        instance.loginWithKakaoAccount(context) { token, error ->
//            if (error != null) {
//                continuation.resumeWithException(error)
//            } else if (token != null) {
//                continuation.resume(token)
//            } else {
//                continuation.resumeWithException(RuntimeException("kakao access token을 받아오는데 실패함, 이유는 명확하지 않음."))
//            }
//        }
//    }
//}

suspend fun UserApiClient.Companion.profile(context: Context): User {
    return suspendCoroutine { continuation ->
        instance.me { user, error ->
            if (error != null) {
                continuation.resumeWithException(error)
            } else if (user != null) {
                val scopes = mutableListOf<String>()
                if (user.kakaoAccount?.emailNeedsAgreement == true) { scopes.add("account_email") }
                if (user.kakaoAccount?.birthdayNeedsAgreement == true) { scopes.add("birthday") }
                if (user.kakaoAccount?.birthyearNeedsAgreement == true) { scopes.add("birthyear") }
                if (user.kakaoAccount?.genderNeedsAgreement == true) { scopes.add("gender") }
                if (user.kakaoAccount?.phoneNumberNeedsAgreement == true) { scopes.add("phone_number") }
                if (user.kakaoAccount?.profileNeedsAgreement == true) { scopes.add("profile") }
                if (user.kakaoAccount?.ageRangeNeedsAgreement == true) { scopes.add("age_range") }
                if (user.kakaoAccount?.ciNeedsAgreement == true) { scopes.add("account_ci") }

                if (scopes.isNotEmpty()) {
                    Log.d("", "사용자에게 추가 동의를 받아야 합니다.")
                    instance.loginWithNewScopes(context, scopes) { token, error ->
                        if (error != null) {
                            Log.e("", "사용자 추가 동의 실패", error)
                            continuation.resumeWithException(error)
                        } else {
                            Log.d("", "allowed scopes: ${token!!.scopes}")
                            instance.me { user, error ->
                                if (error != null) {
                                    Log.e("", "사용자 정보 요청 실패", error)
                                    continuation.resumeWithException(error)
                                }
                                else if (user != null) {
                                    Log.e("", "사용자 정보 요청 성공")
                                    continuation.resume(user)
                                }
                            }
                        }
                    }
                } else {
                    continuation.resume(user)
                }
            } else {
                continuation.resumeWithException(RuntimeException("알수 없는 에러 발생"))
            }
        }
    }
}