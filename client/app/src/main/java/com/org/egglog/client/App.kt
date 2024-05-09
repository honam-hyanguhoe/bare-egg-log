package com.org.egglog.client

import android.app.Application
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.org.egglog.presentation.domain.auth.activity.LoginActivity
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            if(e.message?.contains("HTTP 410") == true && e.message?.contains("retrofit2.HttpException") == true) {
                Toast.makeText(this, "세션만료. 로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
                Log.e("thread: ${t.name.orEmpty()}", "error: ${e.message.orEmpty()}")
            } else if(e.message?.contains("HTTP 418") == true && e.message?.contains("retrofit2.HttpException") == true) {
                Toast.makeText(this, "추가정보가 부족합니다.", Toast.LENGTH_SHORT).show()
                Log.e("thread: ${t.name.orEmpty()}", "error: ${e.message.orEmpty()}")
            } else {
                Toast.makeText(this, "알 수 없는 에러가 발생했습니다.", Toast.LENGTH_SHORT).show()
                Log.e("thread: ${t.name.orEmpty()}", "error: ${e.message.orEmpty()}")
            }
        }
    }
}