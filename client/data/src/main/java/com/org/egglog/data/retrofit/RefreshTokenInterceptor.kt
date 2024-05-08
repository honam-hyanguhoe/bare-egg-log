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
            val tokens = runBlocking { tokenDataStore.getToken() }
            if(tokens.first != null) {
                val newTokens = refreshAccessToken(tokens)
                return if(newTokens != null) {
                    runBlocking { tokenDataStore.setToken(newTokens.accessToken, newTokens.refreshToken) }
                    val newRequest = chain.request().newBuilder()
                        .header("Authorization", "Bearer ${newTokens.accessToken}")
                        .build()
                    response.close()
                    chain.proceed(newRequest)
                } else {
                    response.close()
                    runBlocking { tokenDataStore.clear() }
                    runBlocking { userDataStore.clear() }
                    startLoginActivity()
                    response
                }
            } else {
                response.close()
            }
        }
        return response
    }

    private fun refreshAccessToken(tokens: Pair<String?, String?>): Refresh? {
        return runBlocking { authService.refresh(tokens.second.orEmpty()).dataBody?.toDomainModel() }
    }

    private fun startLoginActivity() {
        context.startActivity(Intent(context, LoginActivity::class.java))
    }
}