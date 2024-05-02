package com.org.egglog.presentation.domain.auth.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.presentation.domain.auth.screen.MainActivity
import com.org.egglog.presentation.domain.auth.screen.SplashScreen
import com.org.egglog.presentation.theme.ClientTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    @Inject lateinit var getTokenUseCase: GetTokenUseCase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            ClientTheme {
                SplashScreen()
            }
        }

        lifecycleScope.launch {
            delay(2000L)
            val isLoggedIn = !getTokenUseCase().first.isNullOrEmpty() || !getTokenUseCase().second.isNullOrEmpty()

            if (isLoggedIn) {
                // 로그인 되었을 떄
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