package com.org.egglog.data.auth.di

import com.org.egglog.domain.auth.usecase.GetRefreshUseCase
import com.org.egglog.domain.auth.usecase.LoginUseCase
import com.org.egglog.data.auth.usecase.LoginUseCaseImpl
import com.org.egglog.data.auth.usecase.GetRefreshUseCaseImpl
import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.data.auth.usecase.GetTokenUseCaseImpl
import com.org.egglog.domain.auth.usecase.SetTokenUseCase
import com.org.egglog.data.auth.usecase.SetTokenUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UserModule {
    @Binds
    abstract fun bindLoginUseCase(uc: LoginUseCaseImpl): LoginUseCase

    @Binds
    abstract fun bindGetRefreshUseCaseImpl(uc: GetRefreshUseCaseImpl): GetRefreshUseCase

    @Binds
    abstract fun bindGetTokenUseCaseImpl(uc: GetTokenUseCaseImpl): GetTokenUseCase

    @Binds
    abstract fun bindSetTokenUseCaseImpl(uc: SetTokenUseCaseImpl): SetTokenUseCase
}