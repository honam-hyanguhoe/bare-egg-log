package com.org.egglog.presentation.domain.auth.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.FirebaseApp
import com.kakao.sdk.common.KakaoSdk
import com.navercorp.nid.NaverIdLoginSDK
import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.presentation.domain.auth.screen.SplashScreen
import com.org.egglog.presentation.theme.ClientTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.org.egglog.presentation.R
import com.org.egglog.presentation.domain.main.activity.MainActivity

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    @Inject lateinit var getTokenUseCase: GetTokenUseCase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        KakaoSdk.init(this, getString(R.string.kakao_native_app_key))
        NaverIdLoginSDK.initialize(this, getString(R.string.naver_client_id), getString(R.string.naver_client_secret), getString(R.string.naver_client_name))
//        val keyHash = Utility.getKeyHash(this)
//        Log.e("Hash", keyHash)
        setContent{
            ClientTheme {
                SplashScreen()
            }
        }

        lifecycleScope.launch {
            val tokens = getTokenUseCase()
            val isLoggedIn = !tokens.first.isNullOrEmpty() && !tokens.second.isNullOrEmpty()
            if (isLoggedIn) {

                startActivity(
                    Intent(
                        this@SplashActivity, MainActivity::class.java
                    ).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                )
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