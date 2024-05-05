package com.org.egglog.presentation.domain.auth.extend

import android.content.Context
import android.util.Log
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private val profileCallback = object : NidProfileCallback<NidProfileResponse> {
    override fun onSuccess(response: NidProfileResponse) {
        val user = response.profile
        Log.e("naver name: ", user?.name.orEmpty())
        Log.e("naver profileImage: ", user?.profileImage.orEmpty())
        Log.e("naver email: ", user?.email.orEmpty())
    }
    override fun onFailure(httpStatus: Int, message: String) {
        val errorCode = NaverIdLoginSDK.getLastErrorCode().code
        val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
        Log.e("naver error: ", "$errorCode $errorDescription")
    }

    override fun onError(errorCode: Int, message: String) {
        onFailure(errorCode, message)
    }
}

val oAuthLoginCallback = object : OAuthLoginCallback {
    override fun onSuccess() {
        NidOAuthLogin().callProfileApi(profileCallback)
    }
    override fun onError(errorCode: Int, message: String) {
        onFailure(errorCode, message)
    }
    override fun onFailure(httpStatus: Int, message: String) {
        val errorCode = NaverIdLoginSDK.getLastErrorCode().code
        val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
        Log.e("naver error: ", "$errorCode $errorDescription")
    }
}

suspend fun authenticateAndGetUserProfile(context: Context): NidProfileResponse {
    return suspendCoroutine { continuation ->
        val callback = object : NidProfileCallback<NidProfileResponse> {
            override fun onSuccess(response: NidProfileResponse) {
                continuation.resume(response)
            }

            override fun onFailure(httpStatus: Int, message: String) {
                continuation.resumeWithException(RuntimeException("Failed to get user profile"))
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }

        val oAuthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                NidOAuthLogin().callProfileApi(callback)
            }

            override fun onError(errorCode: Int, message: String) {
                continuation.resumeWithException(RuntimeException("OAuth login error: $errorCode, $message"))
            }

            override fun onFailure(httpStatus: Int, message: String) {
                continuation.resumeWithException(RuntimeException("OAuth login failure: $httpStatus, $message"))
            }
        }

        NaverIdLoginSDK.authenticate(context, oAuthLoginCallback)
    }
}