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
            if(response.request.url.toString().contains("/auth/refresh")) {
                runBlocking { tokenDataStore.clear() }
                runBlocking { userDataStore.clear() }
                startLoginActivity()
                return response
            }
            val tokens = runBlocking { tokenDataStore.getToken() }
            if(tokens.first != null) {
                val newInfo = refreshAccessToken(tokens)
                return if(newInfo != null) {
                    runBlocking { tokenDataStore.setToken(newInfo.token.accessToken, newInfo.token.refreshToken) }
                    runBlocking { userDataStore.setUser(newInfo.userDetail) }
                    val newRequest = chain.request().newBuilder()
                        .header("Authorization", "Bearer ${newInfo.token.accessToken}")
                        .build()
                    response.close()
                    chain.proceed(newRequest)
                } else {
                    runBlocking { tokenDataStore.clear() }
                    runBlocking { userDataStore.clear() }
                    startLoginActivity()
                    response
                }
            }
        }
        return response
    }

    private fun refreshAccessToken(tokens: Pair<String?, String?>): Refresh? {
        return runBlocking { authService.refresh(tokens.second.orEmpty()).dataBody?.toDomainModel() }
    }

    private fun startLoginActivity() {
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}