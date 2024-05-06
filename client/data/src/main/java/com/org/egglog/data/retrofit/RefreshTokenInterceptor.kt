package com.org.egglog.data.retrofit

import android.content.Context
import android.content.Intent
import com.org.egglog.data.auth.model.toDomainModel
import com.org.egglog.data.auth.service.AuthService
import com.org.egglog.data.datastore.TokenDataStore
import com.org.egglog.data.datastore.UserDataStore
import com.org.egglog.domain.auth.model.Refresh
import com.org.egglog.presentation.domain.auth.activity.LoginActivity
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class RefreshTokenInterceptor @Inject constructor(
    private val tokenDataStore: TokenDataStore,
    private val userDataStore: UserDataStore,
    private val authService: AuthService,
    private val context: Context
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (response.code == 401) {
            val newTokens = refreshAccessToken()
            if(newTokens != null) {
                val newRequest = chain.request().newBuilder()
                    .header("Authorization", "Bearer ${newTokens.accessToken}")
                    .build()
                return chain.proceed(newRequest)
            } else {
                // 로그아웃 시키기
                runBlocking { tokenDataStore.clear() }
                runBlocking { userDataStore.clear() }
                context.startActivity(
                    Intent(
                        context,
                        LoginActivity::class.java
                    )
                )
            }
        }
        return response
    }

    private fun refreshAccessToken(): Refresh? {
        val tokens = runBlocking { tokenDataStore.getToken() }
        val newTokens = runBlocking { authService.refresh(tokens.second!!).dataBody?.toDomainModel() }
        runBlocking {tokenDataStore.setToken(newTokens?.accessToken!!, newTokens.refreshToken) }
        return newTokens
    }
}