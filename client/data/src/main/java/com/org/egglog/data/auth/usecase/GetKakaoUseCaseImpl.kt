package com.org.egglog.data.auth.usecase

import android.content.Context
import android.util.Log
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.org.egglog.data.auth.extend.loginWithKakao
import com.org.egglog.domain.auth.model.Refresh
import com.org.egglog.domain.auth.usecase.GetKakaoUseCase
import javax.inject.Inject

class GetKakaoUseCaseImpl @Inject constructor(
    private val context: Context
): GetKakaoUseCase {
    override suspend fun invoke(): Result<Refresh> {
        return try {
            val oAuthToken = UserApiClient.loginWithKakao(context)
            Log.e("MainActivity", "beanbean > $oAuthToken")
            Result.success(Refresh(refreshToken = "test", accessToken = "test"))
        } catch (error: Throwable) {
            if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                Log.e("MainActivity", "사용자가 명시적으로 취소")
            } else {
                Log.e("MainActivity", "인증 에러 발생: ${error.message}")
            }
            Result.failure(error)
        }
    }
}