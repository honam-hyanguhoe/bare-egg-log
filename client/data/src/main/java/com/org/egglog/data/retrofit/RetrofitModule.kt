package com.org.egglog.data.retrofit

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.org.egglog.data.auth.service.AuthService
import com.org.egglog.data.community.posteditor.service.PostEditorService
import com.org.egglog.data.auth.service.UserService
import com.org.egglog.data.main.service.StaticsService
import com.org.egglog.data.main.service.WorkService
import com.org.egglog.data.community.service.CommunityService
import com.org.egglog.data.group.service.GroupService
import com.org.egglog.data.setting.service.SettingService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Provider

const val HOST = "https://api.egg-log.org"

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
        val jsonConfig = Json {
            coerceInputValues = true
            ignoreUnknownKeys = true
        }
        val converterFactory =
            jsonConfig.asConverterFactory("application/json; charset=UTF8".toMediaType())
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

    @Provides
    fun provideSettingService(retrofit: Retrofit): SettingService {
        return retrofit.create(SettingService::class.java)
    }

    @Provides
    fun providePostingService(retrofit: Retrofit): PostEditorService {
        return retrofit.create(PostEditorService::class.java)
    }

    @Provides
    fun provideWorkService(retrofit: Retrofit): WorkService {
        return retrofit.create(WorkService::class.java)
    }

    @Provides
    fun provideStaticsService(retrofit: Retrofit): StaticsService {
        return retrofit.create(StaticsService::class.java)
    }

    @Provides
    fun provideCommunityService(retrofit: Retrofit): CommunityService {
        return retrofit.create(CommunityService::class.java)
    }

    @Provides
    fun provideGroupService(retrofit: Retrofit): GroupService {
        return retrofit.create(GroupService::class.java)
    }

}