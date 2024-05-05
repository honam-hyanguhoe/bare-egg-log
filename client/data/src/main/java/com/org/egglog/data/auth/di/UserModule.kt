package com.org.egglog.data.auth.di

import com.org.egglog.domain.auth.usecase.GetRefreshUseCase
import com.org.egglog.domain.auth.usecase.LoginUseCase
import com.org.egglog.data.auth.usecase.LoginUseCaseImpl
import com.org.egglog.data.auth.usecase.GetRefreshUseCaseImpl
import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.data.auth.usecase.GetTokenUseCaseImpl
import com.org.egglog.data.auth.usecase.GetUserStoreUseCaseImpl
import com.org.egglog.data.auth.usecase.GetUserUseCaseImpl
import com.org.egglog.domain.auth.usecase.SetTokenUseCase
import com.org.egglog.data.auth.usecase.SetTokenUseCaseImpl
import com.org.egglog.data.auth.usecase.SetUserStoreUseCaseImpl
import com.org.egglog.domain.auth.usecase.GetUserStoreUseCase
import com.org.egglog.domain.auth.usecase.GetUserUseCase
import com.org.egglog.domain.auth.usecase.SetUserStoreUseCase
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

    @Binds
    abstract fun bindGetUserUseCaseImpl(uc: GetUserUseCaseImpl): GetUserUseCase

    @Binds
    abstract fun bindGetUserStoreUseCaseImpl(uc: GetUserStoreUseCaseImpl): GetUserStoreUseCase

    @Binds
    abstract fun bindSetUserStoreUseCaseImpl(uc: SetUserStoreUseCaseImpl): SetUserStoreUseCase
}