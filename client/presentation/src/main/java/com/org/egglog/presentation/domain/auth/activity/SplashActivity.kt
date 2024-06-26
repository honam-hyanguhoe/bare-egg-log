package com.org.egglog.presentation.domain.auth.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.ktx.messaging
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.common.KakaoSdk
import com.navercorp.nid.NaverIdLoginSDK
import com.org.egglog.domain.auth.model.UserFcmTokenParam
import com.org.egglog.domain.auth.usecase.PostRefreshUseCase
import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.domain.auth.usecase.GetUserUseCase
import com.org.egglog.domain.auth.usecase.SetUserStoreUseCase
import com.org.egglog.domain.auth.usecase.UpdateUserFcmTokenUseCase
import com.org.egglog.domain.setting.usecase.SetCalendarGroupMapStoreUseCase
import com.org.egglog.presentation.domain.auth.screen.SplashScreen
import com.org.egglog.presentation.theme.ClientTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.org.egglog.presentation.R
import com.org.egglog.presentation.domain.main.activity.MainActivity
import kotlinx.coroutines.tasks.await

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    @Inject lateinit var getTokenUseCase: GetTokenUseCase
    @Inject lateinit var postRefreshUseCase: PostRefreshUseCase
    @Inject lateinit var getUserUseCase: GetUserUseCase
    @Inject lateinit var setUserStoreUseCase: SetUserStoreUseCase
    @Inject lateinit var setCalendarGroupMapStoreUseCase: SetCalendarGroupMapStoreUseCase
    @Inject lateinit var updateUserFcmTokenUseCase: UpdateUserFcmTokenUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        KakaoSdk.init(this, getString(R.string.kakao_native_app_key))
        NaverIdLoginSDK.initialize(this, getString(R.string.naver_client_id), getString(R.string.naver_client_secret), getString(R.string.naver_client_name))

        setContent {
            ClientTheme {
                SplashScreen()
            }
        }

        startLifecycleScopeWork()
    }

    private fun startLifecycleScopeWork() {
        lifecycleScope.launch {
            val tokens = getTokenUseCase()
            val isLoggedIn = !tokens.first.isNullOrEmpty() && !tokens.second.isNullOrEmpty()
            if (isLoggedIn) {
                Log.e("SplashActivity", "access: " + tokens.first.orEmpty())
                Log.e("SplashActivity", "refresh: " + tokens.second.orEmpty())
                val userDetail = getUserUseCase("Bearer ${tokens.first.orEmpty()}").getOrThrow()
                Log.e("SplashActivity", "user: " + userDetail.toString())
                if (userDetail?.selectedHospital == null || userDetail.empNo == null) {
                    setUserStoreUseCase(userDetail)
                    setCalendarGroupMapStoreUseCase(mapOf(userDetail?.workGroupId.toString() to true))
                    startActivity(
                        Intent(
                            this@SplashActivity, PlusLoginActivity::class.java
                        ).apply {
                            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                    )
                } else {
                    val fcmToken = try {
                        Firebase.messaging.token.await()
                    } catch (e: Exception) {
                        Log.e("FCM Token", "Error fetching FCM token: ${e.message}", e)
                        ""
                    }
                    if (userDetail.deviceToken == null || userDetail.deviceToken != fcmToken) {
                        val newUser = updateUserFcmTokenUseCase("Bearer ${tokens.first.orEmpty()}", UserFcmTokenParam(fcmToken)).getOrThrow()
                        setUserStoreUseCase(newUser)
                        setCalendarGroupMapStoreUseCase(mapOf(newUser?.workGroupId.toString() to true))
                    } else {
                        setUserStoreUseCase(userDetail)
                        setCalendarGroupMapStoreUseCase(mapOf(userDetail.workGroupId.toString() to true))
                    }
                    startActivity(
                        Intent(
                            this@SplashActivity, MainActivity::class.java
                        ).apply {
                            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                    )
                }
            } else {
                // 로그인 안되었을 때
                startActivity(
                    Intent(
                        this@SplashActivity, LoginActivity::class.java
                    ).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                )
            }
        }
    }
}
