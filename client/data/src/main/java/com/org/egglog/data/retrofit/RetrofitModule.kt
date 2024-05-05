package com.org.egglog.data.retrofit

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.org.egglog.data.auth.service.AuthService
import com.org.egglog.data.auth.service.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Provider
import javax.inject.Singleton

const val HOST = "https://api.egg-log.org"

private val json = Json {
    ignoreUnknownKeys = true
}

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {
    @Provides
    fun provideOkHttpClient(
        refreshTokenInterceptorProvider: Provider<RefreshTokenInterceptor>
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain -> refreshTokenInterceptorProvider.get().intercept(chain) }
            .build()
    }


    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        val converterFactory = json.asConverterFactory("application/json; charset=UTF8".toMediaType())
        return Retrofit.Builder()
            .baseUrl("$HOST/v1/")
            .addConverterFactory(converterFactory)
            .client(client)
            .build()
    }

    @Provides
    fun provideAuthService(retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Provides
    fun provideUserService(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }
}