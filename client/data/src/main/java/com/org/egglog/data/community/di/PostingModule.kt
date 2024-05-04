package com.org.egglog.data.community.di

import com.org.egglog.data.community.usecase.WritePostUserCaseImpl
import com.org.egglog.domain.community.usecase.WritePostUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class PostingModule {
    @Binds
    abstract fun bindWritePostUseCase(uc : WritePostUserCaseImpl) : WritePostUseCase
}