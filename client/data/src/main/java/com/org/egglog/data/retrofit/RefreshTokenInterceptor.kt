package com.org.egglog.data.retrofit

import android.content.Context
import android.content.Intent
import com.org.egglog.data.auth.model.toDomainModel
import com.org.egglog.data.auth.service.AuthService
import com.org.egglog.data.datastore.TokenDataStore
import com.org.egglog.data.datastore.UserDataStore
import com.org.egglog.domain.auth.model.Token
import com.org.egglog.presentation.domain.auth.activity.LoginActivity
import com.org.egglog.presentation.domain.auth.activity.PlusLoginActivity
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
        val request = chain.request()
        val response = chain.proceed(request)

        if (response.code == 401) {
            val tokens = runBlocking { tokenDataStore.getToken() }
            if(tokens.first != null) {
                val newTokens = refreshAccessToken(tokens)
                runBlocking { tokenDataStore.setToken(newTokens?.accessToken ?: "", newTokens?.refreshToken ?: "") }
                val newRequest = chain.request().newBuilder()
                    .header("Authorization", "Bearer ${newTokens?.accessToken}")
                    .build()
                response.close()
                return chain.proceed(newRequest)
            }
        } else if(response.code == 410) {
            runBlocking { tokenDataStore.clear() }
            runBlocking { userDataStore.clear() }
            startLoginActivity()
        } else if(response.code == 418) {
            runBlocking { userDataStore.clear() }
            startPlusLoginActivity()
        }
        return response
    }

    private fun refreshAccessToken(tokens: Pair<String?, String?>): Token? {
        return runBlocking { authService.refresh(tokens.second.orEmpty()).dataBody?.toDomainModel() }
    }

    private fun startLoginActivity() {
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    private fun startPlusLoginActivity() {
        val intent = Intent(context, PlusLoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}