package com.org.egglog.client

import android.app.Application
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.org.egglog.presentation.domain.auth.activity.LoginActivity
import com.org.egglog.presentation.domain.auth.activity.PlusLoginActivity
import dagger.hilt.android.HiltAndroidApp
import kotlin.system.exitProcess

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            if(e.message?.contains("HTTP 410") == true && e.message?.contains("retrofit2.HttpException") == true) {
                Toast.makeText(this, "세션만료. 로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
            } else if(e.message?.contains("HTTP 418") == true && e.message?.contains("retrofit2.HttpException") == true) {
                Toast.makeText(this, "추가정보가 부족합니다.", Toast.LENGTH_SHORT).show()
            } else if(e.message?.contains("HTTP 502") == true || e.message?.contains("Failed to connect") == true) {
                Toast.makeText(this, "서버 점검 중입니다.", Toast.LENGTH_SHORT).show()
                exitProcess(1)
            } else if(e.message?.contains("No address associated with hostname") == true){
                startActivity(
                    Intent(this@App, LoginActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK }
                )
                Toast.makeText(this, "네트워크 에러가 발생했습니다.", Toast.LENGTH_SHORT).show()
                exitProcess(1)
            } else if(e.message?.contains("connection closed") == true) {
                startActivity(
                    Intent(this@App, LoginActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK }
                )
                Toast.makeText(this, "통신 에러가 발생했습니다.", Toast.LENGTH_SHORT).show()
                exitProcess(1)
            } else {
                Toast.makeText(this, "알 수 없는 에러가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
            Log.e("thread: ${t.name.orEmpty()}", "error: ${e.message.orEmpty()}")
        }
    }
}